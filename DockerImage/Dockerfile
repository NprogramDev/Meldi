# Use a lightweight Node image
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Install git
RUN apk add --no-cache git

# Clone the repo (shallow clone to reduce size)
RUN git clone --depth 1 https://github.com/NprogramDev/Meldi repo

# Change directory to /app/repo/Server
WORKDIR /app/repo/Server

# Install dependencies if package.json exists
COPY package*.json ./
RUN npm install --production

# Optional: copy entire code (if needed)
# RUN cp -R . .

# Expose port if your app listens on it (optional)
EXPOSE 80

# Define the default command
CMD ["node", "index.js"]
