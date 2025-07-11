{{/*  Labels e nomes reutilizáveis */}}

{{- define "attus.labels" -}}
app.kubernetes.io/managed-by: Helm
app.kubernetes.io/name: attus-processo
{{- end }}

{{- define "attus.fullname" -}}
{{ printf "%s-%s" .Release.Name .Chart.Name }}
{{- end }}

{{- define "attus.selectorLabels" -}}
app.kubernetes.io/name: attus-processo
{{- end }}