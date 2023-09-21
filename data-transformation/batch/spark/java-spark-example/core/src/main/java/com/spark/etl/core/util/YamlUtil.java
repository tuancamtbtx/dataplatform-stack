package com.spark.etl.core.util;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@UtilityClass
public class YamlUtil {
    public static <T> T loadYaml(String filePath, Class<T> valueType) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, valueType);
    }
}
