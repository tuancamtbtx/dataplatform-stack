from airflow.providers.google.cloud.hooks.bigquery import BigQueryHook
from airflow.models import BaseOperator
from airflow.utils.decorators import apply_defaults


class BigQueryToGCSOperator(BaseOperator):
    template_fields = ('sql', 'source_project_dataset_table',
                       'destination_cloud_storage_uris', 'labels')
    template_ext = ()
    ui_color = '#e4e6f0'

    @apply_defaults
    def __init__(self,
                 source_project_dataset_table='',
                 destination_cloud_storage_uris='',
                 sql='',
                 compression=None,
                 export_format='CSV',
                 field_delimiter=',',
                 print_header=True,
                 bigquery_conn_id='bigquery_default',
                 delegate_to=None,
                 labels=None,
                 *args,
                 **kwargs):
        super(BigQueryToGCSOperator, self).__init__(*args, **kwargs)
        if not sql and not source_project_dataset_table:
            raise Exception('Must have sql or source_project_dataset_table')
        self.sql = sql
        self.source_project_dataset_table = source_project_dataset_table
        self.destination_cloud_storage_uris = destination_cloud_storage_uris
        self.compression = compression
        self.export_format = export_format
        self.field_delimiter = field_delimiter
        self.print_header = print_header
        self.bigquery_conn_id = bigquery_conn_id
        self.delegate_to = delegate_to
        self.labels = labels

    def execute(self, context):
        self.log.info('Executing extract of %s into: %s',
                      self.source_project_dataset_table,
                      self.destination_cloud_storage_uris)
        hook = BigQueryHook(gcp_conn_id=self.bigquery_conn_id,
                            delegate_to=self.delegate_to)
        conn = hook.get_conn()
        cursor = conn.cursor()
        if self.sql:
            import hashlib
            tmp_table = 'tmp.seller_center_{}'.format(hashlib.md5(self.sql.encode('utf-8')).hexdigest())
            cursor.run_query(
                sql=self.sql,
                destination_dataset_table=tmp_table,
                write_disposition='WRITE_TRUNCATE',
                allow_large_results=True,
                use_legacy_sql=False,
                create_disposition='CREATE_IF_NEEDED',
            )
            self.source_project_dataset_table = tmp_table


        cursor.run_extract(
            source_project_dataset_table=self.source_project_dataset_table,
            destination_cloud_storage_uris=self.destination_cloud_storage_uris,
            compression=self.compression,
            export_format=self.export_format,
            field_delimiter=self.field_delimiter,
            print_header=self.print_header,
            labels=self.labels)
