import { Link } from "react-router-dom";
import pawsSvg from "./../assets/paws.svg";

function NotFoundPage() {
  return (
    <div className="h-screen w-screen flex flex-col items-center bg-bone text-9xl font-extrabold text-liver justify-center flex-wrap">
      <div className="flex flex-row items-center justify-center">
        4 <img src={pawsSvg} alt="paws" className="w-28" /> 4
      </div>
      <Link to="/" className="text-2xl font-semibold underline">
        Về trang chủ
      </Link>
    </div>
  );
}

export default NotFoundPage;
