supermarket-ims/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── supermarketims/
│       │           ├── controller/               # All application controllers
│       │           │   ├── AdminDashboardController.java
│       │           │   ├── CustomerDashboardController.java
│       │           │   ├── HomeController.java
│       │           │   ├── InventoryController.java
│       │           │   ├── ProductController.java
│       │           │   ├── RefundController.java
│       │           │   ├── SignUoController.java
│       │           │   ├── StaffDashboardController.java
│       │           │   ├── StockOrderController.java
│       │           │   ├── SupplierController.java
│       │           │   └── UserController.java
│       │           │
│       │           ├── model/                    # All entity classes
│       │           │   ├── Cart.java
│       │           │   ├── CartItem.java
│       │           │   ├── Item.java
│       │           │   ├── Order.java
│       │           │   ├── OrderItem.java
│       │           │   ├── OrderItemId.java
│       │           │   ├── OrderStatus.java
│       │           │   ├── Payment.java
│       │           │   ├── PaymentMethod.java
│       │           │   ├── PaymentStatus.java
│       │           │   ├── Refund.java
│       │           │   ├── StockOrder.java
│       │           │   ├── StockOrderItem.java
│       │           │   ├── StockOrderStatus.java
│       │           │   ├── Supplier.java
│       │           │   ├── User.java
│       │           │   └── UserRole.java
│       │           │
│       │           ├── repository/               # Spring Data JPA repositories
│       │           │   ├── CartRepository.java
│       │           │   ├── InventoryRepository.java
│       │           │   ├── OrderItemRepository.java
│       │           │   ├── OrderRepository.java
│       │           │   ├── PaymentRepository.java
│       │           │   ├── StockOrderRepository.java
│       │           │   ├── SupplierRepository.java
│       │           │   └── UserRepository.java
│       │           │
│       │           └── SupermarketImsApplication.java  # Main Spring Boot application
│
│       ├── resources/
│       │   ├── templates/                        # HTML templates
│       │   │   ├── admin-dashboard.html
│       │   │   ├── checkout.html
│       │   │   ├── customer-checkout.html
│       │   │   ├── customer-dashboard.html
│       │   │   ├── customer-payment-success.html
│       │   │   ├── inventory.html
│       │   │   ├── login.html
│       │   │   ├── signup.html
│       │   │   ├── staff-dashboard.html
│       │   │   ├── stock_orders.html
│       │   │   └── user_management.html
│       │   │
│       │   └── application.properties            # Spring Boot config file
│
└── pom.xml                                        # Maven build file
