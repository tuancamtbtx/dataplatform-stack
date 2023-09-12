from fastapi import FastAPI, Request
from fastapi.responses import HTMLResponse
from fastapi.templating import Jinja2Templates
import uvicorn
import os
from airtrust.dynamic.team import TeamLoader

app = FastAPI()

TEMPLATE_PATH  = os.getenv("TEMPLATE_PATH", "/trustingsocial/airtrust/static/templates")
TEAM_CONF = os.getenv("AIRTRUST_TEAM_CONF", "/opt/airflow/team.yaml")
templates = Jinja2Templates(directory="/trustingsocial/airtrust/static/templates")

def load_teams():
	team_loader = TeamLoader("/opt/airflow/team.yaml")
	data =  team_loader.load()
	result = []
	idx = 0
	for team in data.teams:
		item = team.__dict__
		item["branch"] = team.git_repo.branch
		item["repo"] = team.git_repo.repo
		idx +=1
		item["no"] = idx
		result.append(item)
	return result

@app.get("/team",response_class=HTMLResponse)
async def get_list_team(request: Request):
	data = load_teams()
	print(data)
	return templates.TemplateResponse("team.html", {"request": request, "data": data})

@app.get("/create-dag/",response_class=HTMLResponse)
async def hello(request: Request):
	return templates.TemplateResponse("form.html", {"request": request})

if __name__ == "__main__":
	uvicorn.run("airtrust.console.app_server:app", host="0.0.0.0", port=8000, reload=True)