FROM alpine:latest

RUN apk add --no-cache curl

WORKDIR /app
COPY download_qvalues.sh .

RUN chmod +x download_qvalues.sh

ENTRYPOINT ["./download_qvalues.sh"]
