import { getDay, format } from "date-fns";
import PropTypes from "prop-types";

function GridHeaderCell({ date, noChild }) {
  const dayOfWeek = getDay(date);
  const formattedDayOfWeek = dayOfWeek ? `Thứ ${dayOfWeek + 1}` : "Chủ nhật";

  if (noChild) {
    return (
      <div className="w-[calc(calc(100%/8)-0.5rem)] h-16 bg-liver text-bone rounded"></div>
    );
  }

  return (
    <div className="w-[calc(calc(100%/8)-0.5rem)] h-16 bg-liver text-bone rounded flex flex-col justify-center items-center">
      <p className="text-base font-medium">{formattedDayOfWeek}</p>
      <p className="text-xs font-light">{format(date, "dd/LL")}</p>
    </div>
  );
}
GridHeaderCell.propTypes = {
  date: PropTypes.instanceOf(Date),
  noChild: PropTypes.bool,
};

export default GridHeaderCell;
