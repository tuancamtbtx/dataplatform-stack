from typing import Optional, List
from dataclasses import dataclass, field


class RepoType:
    Python = "python"
    Yaml = "yaml"
    Yml = "yml"
    Notebook = "ipynb"

@dataclass
class RepoConfig:
    id: str
    repo: str
    branch: str

@dataclass
class DagConfig:
    id: str
    dag_folder: str
    type: Optional[str]
    

@dataclass
class TeamConfig:
    name: str
    id: str
    owner: str = None
    pool: Optional[str] = None
    git_folder: str = None
    prefix_file: str = None
    render_location: str = None
    role: str = None
    git_repo: RepoConfig = None
    type: Optional[str] = "yaml"
    active: bool = True

    def is_yaml(self):
        return not self.type or self.type == RepoType.Yaml
    
    def is_python(self):
        return self.type == RepoType.Python
    
    # support for DS team
    def is_notebook(self):
        return self.type == RepoType.Notebook
    


@dataclass
class Team():
    teams: List[TeamConfig] = field(default_factory=list)
