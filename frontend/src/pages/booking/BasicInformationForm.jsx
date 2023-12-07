import { useForm, FormProvider } from "react-hook-form";
import { Button, Input, RadioButton } from "../../components/form";
import { FaCheck } from "react-icons/fa";
import { useBookingContext } from "../../contexts/BookingContext";

function BasicInformationForm() {
  const methods = useForm();
  const { handleSubmit } = methods;
  const [{ step }, bookingDispatch] = useBookingContext();

  const isAbleToSubmit = step === 1;
  const onSubmit = handleSubmit((data) => {
    if (!isAbleToSubmit) return;

    const petCount = parseInt(data.petCount, 10);
    bookingDispatch({
      type: "UPDATE_BASIC_INFORMATION",
      payload: { ...data, petCount },
    });
  });

  return (
    <>
      <h3 className="text-3xl mt-4 font-bold">1. Nhập thông tin cơ bản</h3>
      <div className="max-w-sm mt-4 text-liver">
        <FormProvider {...methods}>
          <form onSubmit={onSubmit}>
            <div className="mb-4">
              <div className="font-semibold">- Địa điểm</div>

              <div className="flex flex-row items-center gap-x-4">
                <RadioButton
                  label="Tại nhà"
                  value="AT_HOME"
                  name="location"
                  checked
                />

                <RadioButton
                  label="Tại chi nhánh"
                  value="AT_SHOP"
                  name="location"
                />
              </div>
            </div>

            <div className="mb-4">
              <div className="font-semibold">- Số lượng thú cưng</div>

              <div className="flex flex-row items-center gap-x-4">
                <RadioButton label="1" value="1" name="petCount" checked />

                <RadioButton label="2" value="2" name="petCount" />
              </div>
            </div>

            <Input
              name="note"
              label="- Ghi chú"
              placeholder="Nhập ghi chú"
              type="text"
            />

            {isAbleToSubmit && (
              <Button
                disabled={step > 1}
                type="submit"
                className="mt-4 flex flex-row gap-x-2 items-center"
              >
                <FaCheck />
                Tiếp tục
              </Button>
            )}
          </form>
        </FormProvider>
      </div>
    </>
  );
}

export default BasicInformationForm;
