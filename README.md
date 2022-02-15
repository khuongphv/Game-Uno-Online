# Game-Uno-Online
Game bài uno online, bao gồm server và client 
Gồm:
- Client TCP: (gồm các package: tcp.client.view, tcp.client.assets.cards, tcp.client.control )
    + Giao diện người dùng
    + Gửi các yêu cầu của người dùng (đăng kí, đăng nhập, bạn bè, chơi game,... ) đến server TCP 
    + Nhận và xử lí thông tin từ server TCP
- Server TCP: (gồm các package: tcp.server.view, , tcp.server.control, udp.client.control )
    + Quản lí các yêu cầu người dùng
    + Quản lí gameplay
    + Nhận và xử lí thông tin từ client TCP sau đó gửi trả kết quả
    + Gửi các yêu cầu truy cập cơ sở dữ liệu người dùng đến server UDP
    + Nhận và xử lí thông tin từ server UDP 
- Server UDP: (gồm các package: udp.server.view, udp.server.control, udp.dao)
    + Truy cập và quản lí dữ liệu người dùng
    + Nhận và xử lí thông tin từ server TCP sau đó gửi trả kết quả
