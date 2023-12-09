import { Button } from "../../components/form";
import BookingGrid from "./grid/BookingGrid";
import { FormProvider, useForm } from "react-hook-form";
import { IoIosSend } from "react-icons/io";
import {
  addHours,
  addMinutes,
  format,
  formatISO9075,
  getDay,
  getHours,
  isAfter,
} from "date-fns";
import { useEffect, useMemo } from "react";
import { useBookingContext } from "../../contexts/BookingContext";
import { useMakeAppointment } from "../../hooks";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

function ChoosingTimeForm() {
  const methods = useForm();
  const {
    handleSubmit,
    watch,
    register,
    formState: { errors },
  } = methods;

  const navigate = useNavigate();
  const [{ step, location, note, registeredPets }] = useBookingContext();
  const {
    mutate: makeAppointment,
    isSuccess: isMakeApmtSuccess,
    isError: isMakeApmtError,
  } = useMakeAppointment();

  useEffect(() => {
    if (isMakeApmtError) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại");
    }
  }, [isMakeApmtError]);

  useEffect(() => {
    if (isMakeApmtSuccess) {
      toast.success("Đặt hẹn thành công");
      navigate("/");
    }
  }, [isMakeApmtSuccess]);

  const appointmentDateWatch = watch("appointmentDate", false);
  const date = useMemo(
    () => new Date(appointmentDateWatch),
    [appointmentDateWatch]
  );
  const formattedDate = format(date, "dd/LL/yyyy");
  const isAM = getHours(date) === 7;
  const dayOfWeek = getDay(date);
  const formattedDayOfWeek = dayOfWeek ? `Thứ ${dayOfWeek + 1}` : "Chủ nhật";
  const allowedTimes = useMemo(() => {
    const arr = [];
    const lowerBound = addHours(date, 3);

    let current = date;
    while (!isAfter(current, lowerBound)) {
      arr.push(current);
      current = addMinutes(current, 10);
    }

    return arr;
  }, [date]);

  if (step < 3) {
    return <></>;
  }

  const onSubmit = handleSubmit((data) => {
    const time = formatISO9075(new Date(data.appointmentTime));
    const appointmentLocation = location === "AT_HOME" ? 0 : 1;

    const params = {
      time,
      note,
      location: appointmentLocation,
      appointmentItems: registeredPets,
    };

    makeAppointment(params);
  });

  const appointmentTimeErrorMessage = errors["appointmentTime"]
    ? errors["appointmentTime"].message
    : "";

  return (
    <>
      <h3 className="text-3xl mt-4 font-bold">3. Chọn lịch hẹn</h3>
      <div className="mt-4">
        <FormProvider {...methods}>
          <form onSubmit={onSubmit}>
            <BookingGrid />

            <div className="mt-4 text-base font-semibold">
              {appointmentDateWatch ? (
                <p>
                  - Bạn đã chọn:{" "}
                  <span className="font-bold">
                    {formattedDayOfWeek}, {formattedDate}{" "}
                    {isAM ? "Sáng" : "Chiều"}
                  </span>
                </p>
              ) : (
                <p>- Bạn chưa chọn ngày</p>
              )}
            </div>

            {appointmentDateWatch && (
              <div className="mt-4 flex flex-row items-center gap-x-2 flex-wrap">
                <span className="text-base font-semibold">- Vào lúc:</span>

                <select
                  {...register("appointmentTime", {
                    required: "Vui lòng chọn giờ hẹn",
                  })}
                  className="font-bold outline-none py-2 px-3 rounded"
                >
                  {allowedTimes.map((d, index) => (
                    <option key={index} value={d}>
                      {format(d, "HH:mm")}
                    </option>
                  ))}
                </select>

                <span>giờ</span>

                <span className="text-base font-normal text-red-700 w-full">
                  {appointmentTimeErrorMessage}
                </span>
              </div>
            )}

            <Button type="submit" className="gap-x-2 mt-4">
              <IoIosSend />
              Gửi
            </Button>
          </form>
        </FormProvider>
      </div>
    </>
  );
}

export default ChoosingTimeForm;
