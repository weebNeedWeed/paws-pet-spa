import catPic1 from "./../../assets/cat1.png";
import dogPic1 from "./../../assets/dog1.png";
import Showcase from "./Showcase";

function HomePage() {
  return (
    <div className="flex flex-col items-center">
      <Showcase
        to="/booking"
        btnLabel="Đặt hẹn ngay"
        headingLabel="Chăm sóc mèo bạn một cách tốt nhất tại PAWS"
        src={catPic1}
      />

      <hr className="border-2 border-liver w-2/3" />

      <Showcase
        to="/pricing"
        btnLabel="Xem bảng giá"
        headingLabel="PAWS luôn Cam kết giá cả phải chăng"
        src={dogPic1}
        isReversed
      />
    </div>
  );
}

export default HomePage;
