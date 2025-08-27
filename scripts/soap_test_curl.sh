#!/usr/bin/env bash
set -e
cat > /tmp/soap_req.xml <<'XML'
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:leg="http://legacy.example.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <leg:sayHello>
         <arg0>Renzo</arg0>
      </leg:sayHello>
   </soapenv:Body>
</soapenv:Envelope>
XML

curl -s -X POST http://localhost:8080/legacy   -H "Content-Type: text/xml; charset=utf-8"   --data @/tmp/soap_req.xml | sed -e 's/></>\n</g'
