FROM ubuntu:jammy

WORKDIR /app

RUN apt-get update
RUN apt-get install -y python3
RUN apt-get install -y python3-pip

RUN pip install pymongo
RUN pip install fastapi
RUN pip install "uvicorn[standard]"
RUN pip install requests
RUN pip install jinja2
RUn pip install pydantic

COPY . .

EXPOSE 8000

ENTRYPOINT ["sh", "start.sh"]
