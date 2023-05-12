FROM node:16.14.0
WORKDIR /app
COPY package.json .
COPY yarn.lock .
RUN yarn install
COPY . .
CMD yarn dev