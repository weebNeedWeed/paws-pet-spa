import {
  startOfWeek,
  endOfWeek,
  eachDayOfInterval,
  addDays,
  addHours,
  addWeeks,
  getDay,
} from "date-fns";
import { Button } from "../../../components/form";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import { useState } from "react";
import GridHeaderCell from "./GridHeaderCell";
import GridBodyCell from "./GridBodyCell";
import { useFormContext } from "react-hook-form";

function BookingGrid() {
  const [nextWeek, setNextWeek] = useState(false);
  const {
    reset,
    formState: { errors },
  } = useFormContext();

  // sunday is a day of the next week (by default)
  // so that we need to remove the currentDate by 1 day to get the current week
  let currentDate = new Date();
  if (getDay(currentDate) === 0) {
    currentDate = addDays(currentDate, -1);
  }

  if (nextWeek) {
    currentDate = addWeeks(currentDate, 1);
  }

  const firstDateOfWeek = addDays(startOfWeek(currentDate), 1);
  const lastDateOfWeek = addDays(endOfWeek(currentDate), 1);
  const interval = eachDayOfInterval({
    start: firstDateOfWeek,
    end: lastDateOfWeek,
  });

  const handleMoving = (value) => {
    setNextWeek(value);
    reset();
  };

  const errorMessage = errors["appointmentDate"]
    ? errors["appointmentDate"].message
    : "";

  return (
    <>
      <span className="text-base font-normal text-red-700">{errorMessage}</span>

      <div className="w-full flex flex-row gap-2 flex-wrap">
        <GridHeaderCell noChild />
        {interval.map((date, index) => (
          <GridHeaderCell date={date} key={index} />
        ))}

        <GridBodyCell morning />
        {interval.map((date, index) => (
          <GridBodyCell date={addHours(date, 7)} key={index} />
        ))}

        <GridBodyCell />
        {interval.map((date, index) => (
          <GridBodyCell date={addHours(date, 13)} key={index} />
        ))}
      </div>

      <div className="flex items-center mt-4 gap-x-2">
        <Button
          disabled={!nextWeek}
          onClick={() => handleMoving(false)}
          type="button"
          className="flex items-center gap-x-2 bg-liver disabled:opacity-25"
        >
          <FaArrowLeft />
          Tuần trước
        </Button>

        <Button
          disabled={nextWeek}
          onClick={() => handleMoving(true)}
          type="button"
          className="flex items-center gap-x-2 bg-liver disabled:opacity-25"
        >
          Tuần kế
          <FaArrowRight />
        </Button>
      </div>
    </>
  );
}

export default BookingGrid;
