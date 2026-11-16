# Post-Class 09: Giải thích Exercise 4 và Exercise 6

| Thông tin | Nội dung |
|---|---|
| Họ và tên | Huỳnh Ngọc Khang |
| MSSV | 4651050116 |
| Lớp | CNTT46D |
| Bài | Giải thích Exercise 4 chương 4 và Exercise 6 chương 6 |

Trong bài này, mình giải thích Exercise 4 chương 4 và Exercise 6 chương 6. Mình để kèm các lớp `Card`, `Show`, `Movie`, `Concert` để có thể mở và biên dịch project riêng.

## Môi trường và cấu trúc

- Java 17 trở lên.
- Eclipse IDE for Java Developers.
- Chương 4 dùng `e3.chapter3.Card` trong `src/e3/chapter3`.
- Chương 6 dùng `chapter6.Show`, `Movie`, `Concert` trong `src/chapter6`.
- Lời giải và demo nằm trong `src/postclass09`.

### Mở và chạy bằng Eclipse

Project đã có cấu hình Eclipse trong các file `.project`, `.classpath` và `.settings`. Các lớp mà bài cần đều nằm trong repository, nên khi tải về không phải trỏ tới thư mục khác.

1. Mở Eclipse và chọn hoặc tạo một workspace.
2. Chọn **File > Import... > General > Existing Projects into Workspace**.
3. Ở **Select root directory**, chọn thư mục repository `Postclass09`, rồi đánh dấu project **Postclass09**.
4. Bấm **Finish**. Nếu Eclipse chưa hiện các file mới, nhấn phải vào project và chọn **Refresh**.
5. Mở `Exercise4Demo.java` hoặc `Exercise6Demo.java`, nhấn phải trong editor và chọn **Run As > Java Application**.

Eclipse đặt file biên dịch trong `bin/`. Nếu bật **Build Automatically**, chương trình sẽ được biên dịch khi lưu file.

Mình cũng có thể biên dịch project từ Terminal ở thư mục gốc repository:

```bash
mkdir -p out
javac --release 17 -d out $(find src -name '*.java')
java -cp out postclass09.exercise4.Exercise4Demo
java -cp out postclass09.exercise6.Exercise6Demo
```

Thư mục `out` chứa các file `.class` được tạo khi biên dịch. Xóa thư mục này nếu không cần giữ kết quả chạy.

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

## Exercise 4: Comparator chưa được chọn

### 1. Yêu cầu của đề

Khi tạo `Game`, mình chưa biết luật so sánh các lá bài sẽ dùng. Đề yêu cầu trường `Comparator<Card> aComparator` không nhận `null`. Nếu để trường này bằng `null` rồi gọi `aComparator.compare(...)`, chương trình sẽ gặp `NullPointerException`.

Mình dùng mẫu Null Object để xử lý trạng thái chưa có luật. `Game` khởi tạo sẵn một comparator mặc định, vì vậy trường vẫn luôn giữ một đối tượng có thể gọi được.

### 2. Comparator mặc định hoạt động ra sao?

Trong [Game.java](src/postclass09/exercise4/Game.java), `UndeterminedCardComparator` trả về `0` với mọi cặp bài. Kết quả `0` nghĩa là hai lá bài ngang nhau theo thứ tự đang xét. Ở đây, nó cho biết game chưa áp dụng thứ tự nào.

Khi đã chọn luật chơi, `setComparator` thay comparator mặc định bằng comparator thật. Trong ví dụ, mình dùng `Card.createByRankComparator()` để so sánh theo rank.

Luồng chạy trong demo như sau:

1. Tạo `Game`, comparator mặc định được khởi tạo ngay.
2. Gọi `compare` trước khi chọn luật, kết quả là `0`.
3. Gọi `setComparator` để chọn comparator theo rank.
4. So sánh lại hai lá bài theo luật vừa chọn.

### 3. Chạy ví dụ

`Exercise4Demo` so sánh lá `TWO` với `KING` trước và sau khi chọn comparator theo rank. Lần đầu kết quả là `0`. Sau khi đổi comparator, kết quả âm cho biết `TWO` đứng trước `KING` theo thứ tự trong `Rank`.

```java
Game game = new Game();
game.compare(two, king); // 0: comparator mặc định
game.setComparator(Card.createByRankComparator());
game.compare(two, king); // số âm: TWO đứng trước KING
```

Comparator mặc định này dùng để thể hiện trạng thái chưa chọn luật. Nếu dùng nó trong `TreeSet`, mọi lá bài đều được xem là ngang nhau và `TreeSet` có thể chỉ giữ lại một lá. Vì vậy, mình cần thay comparator mặc định trước khi dùng cấu trúc dữ liệu dựa vào thứ tự.

## Exercise 6: Duyệt các show con bằng Iterator

### 1. Yêu cầu của đề

Composite show có thể gồm nhiều show nhỏ. Bài này yêu cầu client duyệt các show thành phần bằng iterator, đồng thời giữ nguyên interface `Show`.

Mình cho riêng `CompositeShow` triển khai thêm `Iterable<Show>`. `Movie` và `Concert` vẫn dùng `Show` như cũ, còn composite có thêm khả năng duyệt các thành phần.

### 2. Cách cài đặt

Trong [CompositeShow.java](src/postclass09/exercise6/CompositeShow.java), lớp triển khai cả `Show` và `Iterable<Show>`. Constructor nhận từ 2 đến 5 show theo yêu cầu của Exercise 1. Các thành phần được lưu bằng `List.of`, nên danh sách không bị thay đổi sau khi tạo.

Phương thức `iterator()` trả về iterator của danh sách đó. Client có thể đọc từng show con theo thứ tự đã thêm. Lớp cũng giữ các hành vi của một `Show`:

- `description()` nối mô tả của các show con theo thứ tự.
- `runningTime()` cộng thời lượng của các show con.
- `copy()` tạo composite mới và sao chép từng show con.

### 3. Dùng iterator ở client

Trong [Exercise6Demo.java](src/postclass09/exercise6/Exercise6Demo.java), `program` gồm một concert và một composite khác chứa hai phim. Vòng `for-each` lấy lần lượt các thành phần trực tiếp của `program`:

```java
for (Show show : program) {
    System.out.println(show.description());
}
```

Iterator trả về các thành phần trực tiếp. Thành phần thứ hai của program cũng là `CompositeShow`, vì vậy demo lặp thêm lần nữa để lấy hai phim bên trong. Client có iterator khi đang làm việc với `CompositeShow`; các đối tượng `Show` thông thường không có phương thức này.

### 4. Kết quả

`Show` giữ nguyên, nên các lớp `Movie` và `Concert` vẫn tương thích với thiết kế trước. Khi cần tháo composite, client dùng iterator của `CompositeShow` để lấy các phần tử bên trong.

## Tóm tắt

| Bài | Vấn đề | Cách giải quyết | Lợi ích chính |
|---|---|---|---|
| Chapter 4: Exercise 4 | Comparator chưa có luật, tránh gán `null` | Dùng Null Object làm comparator mặc định | Gọi so sánh không gặp `NullPointerException` |
| Chapter 6: Exercise 6 | Client cần lấy show con nhưng không sửa `Show` | Cho `CompositeShow` triển khai `Iterable<Show>` | Duyệt thành phần bằng iterator mà không đổi interface chung |

## Các lớp mẫu đi kèm

Mình giữ các lớp mẫu trong `src/e3/chapter3` và `src/chapter6` để project có đủ kiểu dữ liệu cần dùng. Phần thông tin bản quyền và giấy phép vẫn nằm ở đầu từng file.
