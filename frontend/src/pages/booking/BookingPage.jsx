import { BookingContextProvider } from "../../contexts/BookingContext";
import BasicInformationForm from "./BasicInformationForm";
import ChoosingServiceForm from "./ChoosingServiceForm";
import ChoosingTimeForm from "./ChoosingTimeForm";

function BookingPage() {
  return (
    <div className="text-liver pb-16">
      <h1 className="text-liver text-5xl font-extrabold text-center uppercase">
        Đặt hẹn
      </h1>

      <div className="container max-w-3xl mx-auto">
        <BookingContextProvider>
          <BasicInformationForm />

          <ChoosingServiceForm />

          <ChoosingTimeForm />
        </BookingContextProvider>
      </div>
    </div>
  );
}

export default BookingPage;
