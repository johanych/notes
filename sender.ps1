# Definir destinatarios
$destinatarios = Get-Content -Path "usuSeguridadBP.txt"

# Definir el archivo adjunto
$adjunto = "Template1.html"

# Leer el archivo adjunto y reemplazar el valor de la variable email
$html = Get-Content -Path $adjunto -Raw
foreach ($destinatario in $destinatarios) {
    $nuevo_html = $html -replace 'client=.*?&', "client=$destinatario&"
    
    # Enviar el correo con el archivo adjunto modificado
    Send-MailMessage -SmtpServer pichincha-com.mail.protection.outlook.com -To $destinatario -From 'GOMEZ BUSTOS FELIPE IGNACIO <fgomezbu@pichincha.com>' -Subject "GOMEZ BUSTOS FELIPE IGNACIO compartio un archivo contigo" -BodyAsHtml -Body $nuevo_html
}