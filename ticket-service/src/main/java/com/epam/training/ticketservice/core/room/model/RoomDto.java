package com.epam.training.ticketservice.core.room.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RoomDto {
    private final String roomName;
    private final Integer seatRows;
    private final Integer seatColumns;


    @Override
    public String toString() {
        return "Room "
                + roomName
                + " with " + seatRows*seatColumns + " seats, "
                + seatRows +" rows"
                + " and " + seatColumns + " columns";
    }

    public static class Builder {
        private String roomName;
        private Integer seatRows;
        private Integer seatColumns;

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder withSeatRows(Integer seatRows){
            this.seatRows = seatRows;
            return this;
        }

        public Builder withSeatColumns(Integer seatColumns) {
            this.seatColumns = seatColumns;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(this.roomName, this.seatRows,this.seatColumns);
        }
    }
}
