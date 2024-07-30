curl -X GET \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer glsa_OvsNen9bJllcjVPQ1VSfCouJcukdcVcL_4a16a12d" \
 http://localhost:3000/api/datasources


 curl -X GET \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer glsa_OvsNen9bJllcjVPQ1VSfCouJcukdcVcL_4a16a12d" \
  http://localhost:3000/api/datasources/1 \
  -o my_datasource.json

  curl -X GET "http://localhost:3000/api/dashboards/uid/bdqiuxl7bh98gc" \
-H "Authorization: Bearer glsa_OvsNen9bJllcjVPQ1VSfCouJcukdcVcL_4a16a12d" \
  -H "Content-Type: application/json" \
  -o dashboard.json
