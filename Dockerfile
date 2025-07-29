FROM node:20-alpine
WORKDIR /app
RUN apk add --no-cache git

# Copy code from the repository (GitHub Action checkout)
COPY . .

WORKDIR /app/Server
#RUN npm install --production
VOLUME ["/data"]
EXPOSE 80
CMD ["node", "index.js"]
