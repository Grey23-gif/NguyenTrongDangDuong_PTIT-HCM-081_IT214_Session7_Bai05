# FinBank - Hệ Thống Đăng Ký Khoản Vay (Loan Service) & API Gateway Filter

## 1. Kiến trúc hệ thống
```mermaid
graph TD;
    Client[Client / Postman] -->|Port 8222| Gateway[API Gateway Service]; extreme
    subgraph Gateway Filters
        Gateway --> LoggingFilter[LoggingFilter - GlobalFilter]
    end
    Gateway -->|/api/customers/**| CustomerService[Customer Service - Port 8081]
    Gateway -->|/api/accounts/**| AccountService[Account Service - Port 8082]
    Gateway -->|/api/loans/**| LoanService[Loan Service - Port 8083]
    
    LoanService -->|FeignClient GET /api/customers/{id}| CustomerService
    LoanService -->|FeignClient GET /api/accounts/customer/{id}| AccountService
```

## 2. Mô tả Gateway Filter (LoggingFilter)
- **Loại filter:** `GlobalFilter`, `Ordered` (order = -1).
- **Chức năng:**
  1. Ghi log Request Method, URI path và thời điểm bắt đầu request.
  2. Khi response chuẩn bị gửi lại client, tính tổng thời gian xử lý (`duration`).
  3. Đính kèm custom header `X-Response-Time: {duration}ms` vào Response Header.
  4. Ghi log Status code và thời gian phản hồi.

## 3. Luồng xử lý Đăng ký khoản vay (`POST /api/loans/apply`)
1. Client gửi request đăng ký vay qua API Gateway.
2. **Loan Service** sử dụng `CustomerServiceClient` để kiểm tra thông tin khách hàng.
3. **Loan Service** sử dụng `AccountServiceClient` để lấy danh sách tài khoản của khách hàng và kiểm tra xem có tài khoản nào `ACTIVE` để nhận giải ngân hay không.
4. Tính toán số tiền lãi, tổng tiền phải trả và số tiền trả hàng tháng (với lãi suất 8%/năm).
5. Lưu bản ghi Khoản vay vào cơ sở dữ liệu với trạng thái `PENDING`.
6. Trả về thông tin chi tiết khoản vay bao gồm thông tin khách hàng và số tài khoản nhận giải ngân.