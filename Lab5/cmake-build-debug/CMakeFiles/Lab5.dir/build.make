# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.25

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /mnt/d/GitHub/PDP/Lab5

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /mnt/d/GitHub/PDP/Lab5/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Lab5.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/Lab5.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/Lab5.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Lab5.dir/flags.make

CMakeFiles/Lab5.dir/main.cpp.o: CMakeFiles/Lab5.dir/flags.make
CMakeFiles/Lab5.dir/main.cpp.o: /mnt/d/GitHub/PDP/Lab5/main.cpp
CMakeFiles/Lab5.dir/main.cpp.o: CMakeFiles/Lab5.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/mnt/d/GitHub/PDP/Lab5/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Lab5.dir/main.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/Lab5.dir/main.cpp.o -MF CMakeFiles/Lab5.dir/main.cpp.o.d -o CMakeFiles/Lab5.dir/main.cpp.o -c /mnt/d/GitHub/PDP/Lab5/main.cpp

CMakeFiles/Lab5.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Lab5.dir/main.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /mnt/d/GitHub/PDP/Lab5/main.cpp > CMakeFiles/Lab5.dir/main.cpp.i

CMakeFiles/Lab5.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Lab5.dir/main.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /mnt/d/GitHub/PDP/Lab5/main.cpp -o CMakeFiles/Lab5.dir/main.cpp.s

# Object files for target Lab5
Lab5_OBJECTS = \
"CMakeFiles/Lab5.dir/main.cpp.o"

# External object files for target Lab5
Lab5_EXTERNAL_OBJECTS =

Lab5: CMakeFiles/Lab5.dir/main.cpp.o
Lab5: CMakeFiles/Lab5.dir/build.make
Lab5: CMakeFiles/Lab5.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/mnt/d/GitHub/PDP/Lab5/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable Lab5"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Lab5.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Lab5.dir/build: Lab5
.PHONY : CMakeFiles/Lab5.dir/build

CMakeFiles/Lab5.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Lab5.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Lab5.dir/clean

CMakeFiles/Lab5.dir/depend:
	cd /mnt/d/GitHub/PDP/Lab5/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /mnt/d/GitHub/PDP/Lab5 /mnt/d/GitHub/PDP/Lab5 /mnt/d/GitHub/PDP/Lab5/cmake-build-debug /mnt/d/GitHub/PDP/Lab5/cmake-build-debug /mnt/d/GitHub/PDP/Lab5/cmake-build-debug/CMakeFiles/Lab5.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Lab5.dir/depend

