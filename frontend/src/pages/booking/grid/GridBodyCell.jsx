import clsx from "clsx";
import { isBefore } from "date-fns";
import PropTypes from "prop-types";
import { useId } from "react";
import { useFormContext } from "react-hook-form";
import { FaCheck } from "react-icons/fa";

function GridBodyCell({ morning, date }) {
  const id = useId();
  const { register, resetField, getValues } = useFormContext();

  if (!date) {
    return (
      <div className="w-[calc(calc(100%/8)-0.5rem)] h-32 bg-liver rounded flex flex-row justify-center items-center text-lg text-bone font-medium">
        {morning ? "Sáng" : "Chiều"}
      </div>
    );
  }

  const handleClick = () => {
    resetField("appointmentTime", {
      defaultValue: getValues("appointmentDate"),
    });
  };

  const isAbleToPick = !isBefore(date, new Date());

  return (
    <div className="w-[calc(calc(100%/8)-0.5rem)] h-32">
      <input
        {...register("appointmentDate", { required: "Vui lòng chọn ngày hẹn" })}
        type="radio"
        className="hidden peer"
        value={date}
        id={id}
        disabled={!isAbleToPick}
      />
      <label
        onClick={handleClick}
        htmlFor={id}
        className={clsx(
          "h-full w-full cursor-pointer bg-camel rounded flex flex-row justify-center items-center text-lg text-bone font-medium border-4 border-camel hover:border-liver hover:text-liver",
          "peer-checked:border-liver peer-checked:text-liver transition-all",
          isAbleToPick ? "opacity-100" : "opacity-60"
        )}
      >
        <FaCheck />
      </label>
    </div>
  );
}
GridBodyCell.propTypes = {
  morning: PropTypes.bool,
  date: PropTypes.instanceOf(Date),
};

export default GridBodyCell;
