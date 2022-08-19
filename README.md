# WeatherApp
* Ứng dụng tổng hợp các kiến thức cơ bản về Android.
* Project của môn *****[Phát triển ứng dụng cho các thiết bị di động](https://www.youtube.com/playlist?list=PLD8zSU7U1L2GVhpPIUlJegpP8HRC2r58w)***** (D18).
* Kết quả đạt được sau các khóa học *****[Android Basics in Kotlin](https://developer.android.com/courses/android-basics-kotlin/course)*****, một phần của *****[Advanced Android in Kotlin](https://developer.android.com/courses/kotlin-android-advanced/overview)*****, cùng một số codelab khác. Tất cả cung cấp bởi Google.

## Thông tin cá nhân
1. Họ tên: Trần Đỗ Minh
2. Mã sinh viên: B18DCCN411
3. Nhóm: 01

## Các chức năng đã cài đặt
1. Lấy, cache và hiển thị thông tin thời tiết từ API của MetaWeather `https://www.metaweather.com/api/`. Tự động cập nhật và gửi thông báo mỗi 3 giờ. **(Chức năng này hiện không hoạt động được do server của MetaWeather đã sập từ 18/5/2022)**
2. Thêm, sửa, xóa, tìm kiếm và lọc nhắc việc. Đặt và xóa thông báo cho nhắc việc. Đăng ký, đăng nhập để sử dụng chức năng, đăng xuất, quên mật khẩu.
3. Lấy, cache và hiển thị thông tin thời tiết từ API của OpenWeather `https://openweathermap.org/api`. Tự động cập nhật và gửi thông báo mỗi 3 giờ. Xem thời tiết theo vị trí hiện tại của thiết bị.

## Các kiến thức sử dụng
1. Material Design 3
2. Kiến trúc MVVM, Repository pattern, 2-layer: UI - Data
3. DataBinding & ViewBinding
4. Navigation component với SafeArgs, DeepLink & BottomNavigationView
5. ViewModel & LiveData
6. Retrofit & Moshi
7. Coil
8. RecyclerView với ListAdapter
9. Coroutines & Flow
10. BindingAdapter
11. Room & SQLCipher
12. Preferences DataStore
13. WorkManager & Notifications
14. FirebaseAuthentication với FirebaseUI
15. Google Play Services Location
16. R8 & ProGuard rules cho bản release

## Chú ý
* Để build project, cần thêm file `google-services.json` sinh bởi Firebase.