FROM node:16-alpine as build
RUN apk add --no-cache libc6-compat
WORKDIR /app

COPY package.json package-lock.json  ./
RUN npm install

COPY . .
RUN npm run build

EXPOSE 3000
ENV PORT 3000

CMD ["npm", "run", "start"]