{{/* Define a helper to generate the full MySQL connection URL */}}
{{- define "my-pfa-backend.fullMySQLConnectionURL" -}}
jdbc:mysql://{{ .Release.Name }}-mysql-service:3300/{{ .Values.mysql.databaseName }}?createDatabaseIfNotExist=true&characterEncoding=UTF-8&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true
{{- end -}}