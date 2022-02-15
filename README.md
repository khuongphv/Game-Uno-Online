# Game-Uno-Online
Game bài uno online, bao gồm server và client 
Gồm:
- Client TCP:
    + Giao diện người dùng
    + Gửi các yêu cầu của người dùng (đăng kí, đăng nhập, bạn bè, chơi game,... ) đến server TCP 
    + Nhận và xử lí thông tin từ server TCP
- Server TCP: 
    + Quản lí các yêu cầu người dùng
    + Quản lí gameplay
    + Nhận và xử lí thông tin từ client TCP sau đó gửi trả kết quả
    + Gửi các yêu cầu truy cập cơ sở dữ liệu người dùng đến server UDP
    + Nhận và xử lí thông tin từ server UDP 
- Server UDP: 
    + Truy cập và quản lí dữ liệu người dùng
    + Nhận và xử lí thông tin từ server TCP sau đó gửi trả kết quả
