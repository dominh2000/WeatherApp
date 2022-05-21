# WeatherApp
Ứng dụng tổng hợp các kiến thức cơ bản về Android

## Các chức năng đã cài đặt
1. Lấy, cache và hiển thị thông tin thời tiết từ API của MetaWeather `https://www.metaweather.com/api/`. Tự động cập nhật và gửi thông báo mỗi 3 giờ. **(Chức năng này hiện không hoạt động được do server của MetaWeather đã sập từ 18/5/2022)**
2. Thêm, sửa, xóa, tìm kiếm và lọc nhắc việc. Đặt và xóa chuông báo cho nhắc việc. Đăng ký, đăng nhập để sử dụng chức năng, đăng xuất, quên mật khẩu.
3. Lấy, cache và hiển thị thông tin thời tiết từ API của OpenWeather `https://openweathermap.org/api`. Tự động cập nhật và gửi thông báo mỗi 3 giờ.

## Sử dụng phối hợp các thư viện và chức năng sau
1. DataBinding & ViewBinding
2. Navigation component với SafeArgs & BottomNavigationView
3. ViewModel & LiveData
4. Retrofit & Moshi
5. Coil
6. RecyclerView với ListAdapter
7. Coroutines
8. BindingAdapter
9. Room & Flow
10. WorkManager & Notifications
11. Firebase Authentication với FirebaseUI
12. MVVM, Repository pattern, 3-layer: UI - Domain (model) - Data