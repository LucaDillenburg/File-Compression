MAIN ?= main
EXEC ?= lzw
CFLAGS = -Wall -O2 -g
CC = gcc

BUILD_DIR ?= ./build
SRC_DIRS ?= ./src

SRCS := $(shell find $(SRC_DIRS) -name *.c)
OBJS := $(SRCS:%=$(BUILD_DIR)/%.o)

INC_DIRS := $(shell find $(SRC_DIRS) -type d)
INC_FLAGS := $(addprefix -I,$(INC_DIRS))

$(BUILD_DIR)/$(MAIN): $(OBJS)
	$(CC) $(OBJS) -o $(EXEC) $(LDFLAGS)

$(BUILD_DIR)/%.c.o: %.c
	$(MKDIR_P) $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

debug:
	make
	-gdb ./$(BUILD_DIR)/$(MAIN)
	make clean

run:
	make
	-./$(BUILD_DIR)/$(MAIN)
	make clean

clean:
	-rm -r $(BUILD_DIR)

MKDIR_P ?= mkdir -p
