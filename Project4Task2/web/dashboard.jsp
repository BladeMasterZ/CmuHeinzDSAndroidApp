<%-- 
    Document   : dashboard
    Created on : Mar 31, 2018, 8:23:57 PM
    Author     : zhangxian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DashBoard</title>
    </head>
    <body>
        
        
        
<c:choose>
    <c:when test="${noDataVerify =='1'}">
        <h1 align="center"><c:out value="${noData}"/></h1>
        <br />
    </c:when>    
    <c:otherwise>
        <h1 align="center">Operations Analytics</h1>
        
        <br>
        <br>
        
        <h3 align="center">1: The Highest Exchange Rate: <c:out value="${largestRate}"/></h3>
        <br>
        <br>
        <h3 align="center">2:Average Search Latency: <c:out value="${AvgLatancy}"/> milliseconds</h3>
        <br>
        <br>
        <h3 align="center">3: Frequency Rates of Searching Requests: </h3>
        <div id="container" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>
        
<table border="1" align="center" > 
<tr>
  <td> <c:out value="Serial No"/></td>  
  <td> <c:out value="Search Latency"/></td>
  <td> <c:out value="User IP" /></td>
  <td> <c:out value="Phone Model" /></td>
  <td> <c:out value="Search Currency" /></td>
  <td> <c:out value="Currency Exchange Rates" /></td>
  <td> <c:out value="Last Accessed Date" /></td>
</tr> 


<c:forEach var="data" items="${dataList}">


<tr>
  <td> <c:out value="${data.serNo}"/></td>
  <td> <c:out value="${data.userType}"/></td>
  <td> <c:out value="${data.userIP}" /></td>
  <td> <c:out value="${data.baseCurrency}" /></td>
  <td> <c:out value="${data.searchCurrency}" /></td>
  <td> <c:out value="${data.rates}" /></td>
  <td> <c:out value="${data.lastAccessed}" /></td>
</tr> 

</c:forEach>

</table>
    </c:otherwise>
</c:choose>
        

<script>
Highcharts.chart('container', {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    title: {
        text: '3: Frequency'
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            }
        }
    },
    series: [{
        name: 'Brands',
        colorByPoint: true,
        data: [{
            name: 'USD',
            y: ${counUSDrate},
           
        },  {
            name: 'INR',
            y: ${counINRrate},
        }, {
            name: 'AUD',
            y: ${counAUDrate},
        }, {
            name: 'CAD',
            y:  ${counCADrate},
        }, {
            name: 'CNY',
            y:  ${counCNYrate},
        }]
    }]
});
</script>

    </body>
</html>
