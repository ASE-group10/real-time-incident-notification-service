# Build stage
FROM rust:1.82 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the files from your machine to the container
COPY ./ ./

# Build the program for release
RUN cargo build --release

# Runtime stage
FROM debian:bookworm-slim AS runtime

# Install required shared libraries
RUN apt-get update && apt-get install -y \
    libssl3 \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled binary from the build stage
COPY --from=build /app/target/release/real-time-incident-notification-service /usr/local/bin

# Set the entrypoint
ENTRYPOINT ["/usr/local/bin/real-time-incident-notification-service"]
