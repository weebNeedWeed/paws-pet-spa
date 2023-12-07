import { useBookingContext } from "../../contexts/BookingContext";
import PetInformationForm from "./PetInformationForm";

function ChoosingServiceForm() {
  const [bookingState] = useBookingContext();
  const { step, petCount } = bookingState;

  if (step < 2) {
    return <></>;
  }

  return (
    <>
      <h3 className="text-3xl mt-4 font-bold">2. Đăng ký dịch vụ</h3>
      <div className="mt-4 text-liver">
        {Array.from(Array(petCount)).map((_, index) => (
          <PetInformationForm key={index} petNum={index + 1} />
        ))}
      </div>
    </>
  );
}

export default ChoosingServiceForm;
