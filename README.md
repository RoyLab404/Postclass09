# Post-Class 09 — Giải thích Exercise 4 và Exercise 6

Tài liệu này giải thích hai bài được giao trong Post-Class 09. Phần mã Java giải quyết Exercise 4 chương 4 và Exercise 6 chương 6. Các lớp nền `Card`, `Show`, `Movie`, `Concert` được đưa kèm trong project để có thể mở và biên dịch độc lập.

## Môi trường và cấu trúc

- Java 17 trở lên.
- Eclipse IDE for Java Developers.
- Chương 4 sử dụng `e3.chapter3.Card` trong `src/e3/chapter3`.
- Chương 6 sử dụng `chapter6.Show`, `Movie`, `Concert` trong `src/chapter6`.
- Lời giải và demo đặt trong `src/postclass09`.

### Mở và chạy bằng Eclipse

Thư mục này đã có cấu hình project Eclipse (`.project`, `.classpath`, `.settings`). Các lớp cần thiết đều nằm bên trong project nên khi clone repository, Eclipse không cần đường dẫn tới thư mục nào khác.

1. Mở Eclipse và chọn hoặc tạo một workspace.
2. Chọn **File → Import… → General → Existing Projects into Workspace**.
3. Ở **Select root directory**, chọn thư mục repository `Postclass09`, rồi đánh dấu project **Postclass09**.
4. Bấm **Finish**. Nếu Eclipse chưa cập nhật thay đổi, nhấn phải project và chọn **Refresh**.
5. Mở `Exercise4Demo.java` hoặc `Exercise6Demo.java`, nhấn phải trong editor, chọn **Run As → Java Application**.

Project đặt output vào `bin/`; Eclipse tự biên dịch khi lưu file.

Có thể biên dịch toàn bộ project từ thư mục gốc repository bằng lệnh:

```bash
mkdir -p out
javac --release 17 -d out $(find src -name '*.java')
java -cp out postclass09.exercise4.Exercise4Demo
java -cp out postclass09.exercise6.Exercise6Demo
```

Thư mục `out` chỉ chứa file `.class` đã biên dịch; có thể xóa sau khi chạy xong.

```text
Postclass09/
├── README.md
└── src/
    ├── chapter6/                 # Show, Movie, Concert dùng làm ví dụ
    ├── e3/chapter3/              # Card, Rank, Suit dùng làm ví dụ
    └── postclass09/
        ├── exercise4/            # Game và demo comparator
        └── exercise6/            # CompositeShow và demo iterator
```

## Exercise 4 — Comparator chưa được chọn

### 1. Đề bài muốn giải quyết điều gì?

Trong lớp `Game` có trường `Comparator<Card> aComparator`. Khi tạo một ván chơi, luật sắp xếp bài có thể chưa được quyết định. Nếu gán `null` cho trường này thì những chỗ gọi `aComparator.compare(...)` có thể bị `NullPointerException`.

Yêu cầu là giữ nguyên kiểu `Comparator<Card>` và vẫn có một comparator dùng được ngay từ lúc khởi tạo. Cách mình chọn là dùng **Null Object**: tạo một comparator đại diện cho trạng thái “chưa có luật so sánh”.

### 2. Cách cài đặt

Trong [Game.java](src/postclass09/exercise4/Game.java), `aComparator` được khởi tạo bằng `UndeterminedCardComparator`. Comparator này trả về `0` với mọi cặp bài. Theo quy ước của `Comparator`, kết quả `0` nghĩa là hai đối tượng được xem như ngang nhau theo thứ tự đang xét. Ở đây nó biểu thị rằng trò chơi chưa chọn thứ tự nào.

Khi luật đã được quyết định, `setComparator` thay thế đối tượng mặc định bằng comparator thật. Ví dụ dùng `Card.createByRankComparator()` đã được cung cấp cùng lớp `Card` trong repo.

Luồng xử lý có thể hiểu đơn giản như sau:

1. Tạo `Game`: trường comparator nhận đối tượng mặc định, không phải `null`.
2. Gọi `compare`: lời gọi luôn đi qua một đối tượng hợp lệ.
3. Chọn luật chơi: truyền comparator thật vào `setComparator`.
4. Những lần so sánh tiếp theo dùng luật mới.

### 3. Chạy ví dụ

`Exercise4Demo` tạo hai lá bài, so sánh trước và sau khi chọn luật so sánh theo rank. Trước khi chọn luật, kết quả là `0`; sau đó `TWO` được xếp trước `KING` theo thứ tự enum của `Rank`.

```java
Game game = new Game();
game.compare(two, king); // 0: comparator mặc định
game.setComparator(Card.createByRankComparator());
game.compare(two, king); // số âm: TWO đứng trước KING
```

### 4. Điểm cần lưu ý

Comparator mặc định này chỉ phù hợp để biểu diễn trạng thái “chưa xác định”. Nếu dùng nó làm comparator cho `TreeSet` hoặc cấu trúc cần phân biệt các lá bài theo thứ tự, tất cả bài sẽ bị xem là tương đương; `TreeSet` có thể chỉ giữ lại một phần tử. Vì vậy chương trình nên thay comparator trước khi bắt đầu thao tác cần một thứ tự thật.

## Exercise 6 — Duyệt các show con bằng Iterator

### 1. Đề bài muốn giải quyết điều gì?

Các bài trước tạo một show ghép từ nhiều show thành phần. Bài 6 yêu cầu client lấy các phần tử bên trong để “tháo” composite, nhưng không được thêm phương thức vào interface `Show`.

Nếu thêm `iterator()` trực tiếp vào `Show`, mọi lớp cài đặt `Show` đều phải thay đổi. Thay vào đó, lớp composite có thể cung cấp khả năng lặp riêng bằng cách triển khai thêm interface chuẩn `Iterable<Show>`. `Show` vẫn giữ nguyên như trước.

### 2. Cách cài đặt

Trong [CompositeShow.java](src/postclass09/exercise6/CompositeShow.java), lớp vừa `implements Show`, vừa `implements Iterable<Show>`. Danh sách thành phần được lưu bất biến bằng `List.of`, và constructor kiểm tra composite có từ 2 đến 5 show theo yêu cầu của Exercise 1.

Phương thức `iterator()` trả về iterator của danh sách thành phần. Do danh sách không thể sửa sau khi tạo, client có thể đọc các show con nhưng không thể dùng iterator để thêm hoặc xóa thành phần của composite.

Lớp cũng cài đặt ba hành vi cơ bản của `Show` để ví dụ dùng được như một show bình thường:

- `description()` ghép mô tả của các show con theo đúng thứ tự.
- `runningTime()` cộng thời lượng của các show con.
- `copy()` tạo composite mới và sao chép từng thành phần.

### 3. Dùng iterator ở client

Trong [Exercise6Demo.java](src/postclass09/exercise6/Exercise6Demo.java), composite ngoài gồm một concert và một composite khác chứa hai phim. Vì `CompositeShow` là `Iterable<Show>`, client dùng vòng `for-each` để đọc trực tiếp từng phần tử:

```java
for (Show show : program) {
    System.out.println(show.description());
}
```

Iterator này trả về **các thành phần trực tiếp** của composite hiện tại. Trong ví dụ, phần tử thứ hai của `program` bản thân nó cũng là `CompositeShow`; muốn đi sâu tới hai phim bên trong thì client tiếp tục lặp trên composite đó. Cách này giữ API `Show` ổn định, đồng thời chỉ những đối tượng thực sự là composite mới có iterator.

### 4. Vì sao cách này đáp ứng yêu cầu?

`Show` không bị thay đổi, nên các lớp `Movie` và `Concert` hiện có vẫn tương thích. Client muốn tháo một composite phải có đối tượng composite để lặp; một `Show` bất kỳ không tự động có iterator. Đây là sự đánh đổi có chủ đích để không bắt mọi loại show cung cấp thao tác chỉ có ý nghĩa với composite.

## Tóm tắt

| Bài | Vấn đề | Cách giải quyết | Lợi ích chính |
|---|---|---|---|
| Chapter 4 — Exercise 4 | Comparator chưa có luật, tránh gán `null` | Dùng Null Object làm comparator mặc định | Gọi so sánh không gặp `NullPointerException` |
| Chapter 6 — Exercise 6 | Client cần lấy show con nhưng không sửa `Show` | Cho `CompositeShow` triển khai `Iterable<Show>` | Duyệt thành phần bằng iterator mà không đổi interface chung |

## Các lớp mẫu đi kèm

Các file trong `src/e3/chapter3` và `src/chapter6` là các lớp mẫu nền được giữ kèm để project chạy độc lập. Thông tin bản quyền và giấy phép gốc được giữ trong đầu mỗi file.
