#!/bin/bash

# SOAP testing script for Legacy Orders Java 6 project
# Tests the JAX-WS SOAP endpoints

echo "============================================================"
echo "TESTING LEGACY SOAP SERVICE"
echo "============================================================"

SOAP_URL="http://localhost:8080/legacy"
WSDL_URL="http://localhost:8080/legacy?wsdl"

# Function to check if service is running
check_service() {
    echo "Checking if SOAP service is running..."
    
    # Try to get WSDL
    if curl -s --connect-timeout 5 "$WSDL_URL" > /dev/null 2>&1; then
        echo "✓ SOAP service is running at $SOAP_URL"
        return 0
    else
        echo "✗ SOAP service is not running"
        echo "Start the application first: ./scripts/run_java6.sh"
        return 1
    fi
}

# Function to test SOAP operations
test_soap_operations() {
    echo ""
    echo "=== TESTING SOAP OPERATIONS ==="
    
    # Test 1: Get WSDL
    echo ""
    echo "1. Testing WSDL retrieval..."
    curl -s "$WSDL_URL" | head -5
    if [ ${PIPESTATUS[0]} -eq 0 ]; then
        echo "✓ WSDL retrieved successfully"
    else
        echo "✗ Failed to retrieve WSDL"
    fi
    
    # Test 2: Create Order
    echo ""
    echo "2. Testing createOrder operation..."
    
    cat > /tmp/soap_create_order.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:leg="http://legacy.example.com/">
   <soap:Header/>
   <soap:Body>
      <leg:createOrder>
         <customerId>1001</customerId>
         <customerName>Test Customer</customerName>
         <customerEmail>test@example.com</customerEmail>
         <productName>Test Product</productName>
         <quantity>2</quantity>
         <unitPrice>99.99</unitPrice>
      </leg:createOrder>
   </soap:Body>
</soap:Envelope>
EOF
    
    RESPONSE=$(curl -s -X POST \
        -H "Content-Type: text/xml; charset=utf-8" \
        -H "SOAPAction: \"\"" \
        -d @/tmp/soap_create_order.xml \
        "$SOAP_URL")
    
    if [[ $RESPONSE == *"orderId"* ]]; then
        echo "✓ Order created successfully"
        
        # Extract order ID from response
        ORDER_ID=$(echo "$RESPONSE" | grep -o '<orderId>[0-9]*</orderId>' | sed 's/<[^>]*>//g')
        echo "  Order ID: $ORDER_ID"
    else
        echo "✗ Failed to create order"
        echo "Response: $RESPONSE"
    fi
    
    # Test 3: Get Order (if we have an ORDER_ID)
    if [ ! -z "$ORDER_ID" ]; then
        echo ""
        echo "3. Testing getOrderAsXml operation..."
        
        cat > /tmp/soap_get_order.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:leg="http://legacy.example.com/">
   <soap:Header/>
   <soap:Body>
      <leg:getOrder>
         <orderId>$ORDER_ID</orderId>
      </leg:getOrder>
   </soap:Body>
</soap:Envelope>
EOF
        
        RESPONSE=$(curl -s -X POST \
            -H "Content-Type: text/xml; charset=utf-8" \
            -H "SOAPAction: \"\"" \
            -d @/tmp/soap_get_order.xml \
            "$SOAP_URL")
        
        if [[ $RESPONSE == *"<order"* ]]; then
            echo "✓ Order retrieved successfully"
            echo "  Response contains order XML data"
        else
            echo "✗ Failed to retrieve order"
        fi
    fi
    
    # Test 4: Get Server Info
    echo ""
    echo "4. Testing getServerInfo operation..."
    
    cat > /tmp/soap_server_info.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:leg="http://legacy.example.com/">
   <soap:Header/>
   <soap:Body>
      <leg:getServerInfo/>
   </soap:Body>
</soap:Envelope>
EOF
    
    RESPONSE=$(curl -s -X POST \
        -H "Content-Type: text/xml; charset=utf-8" \
        -H "SOAPAction: \"\"" \
        -d @/tmp/soap_server_info.xml \
        "$SOAP_URL")
    
    if [[ $RESPONSE == *"LEGACY SOAP SERVICE"* ]]; then
        echo "✓ Server info retrieved successfully"
        echo "✓ Confirmed: Using JAX-WS from Java 6 JDK"
        echo "✗ WARNING: Will BREAK in Java 11+ (JAX-WS removed)"
    else
        echo "✗ Failed to get server info"
    fi
    
    # Clean up temp files
    rm -f /tmp/soap_*.xml
}

# Main execution
if check_service; then
    test_soap_operations
    
    echo ""
    echo "============================================================"
    echo "SOAP TESTING COMPLETED"
    echo "============================================================"
    echo "Service URL: $SOAP_URL"
    echo "WSDL URL: $WSDL_URL"
    echo ""
    echo "To test manually:"
    echo "curl '$WSDL_URL' # Get WSDL"
    echo "curl -X POST -H 'Content-Type: text/xml' \\"
    echo "     -d @your_soap_request.xml '$SOAP_URL'"
    echo "============================================================"
else
    exit 1
fi