import copy

def check_puzzle_full(puzzle_grid):
    for r in range(9):
        for c in range(9):
            if int(puzzle_grid[r][c]) == 0:
                return 0
    return 1

def return_box(puzzle_grid, row, column):
    box = []
    
    if row <=3 and column <= 3:
        for r in range(3):
            for c in range(3):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=3 and column <= 6:
        for r in range(3):
            for c in range(3, 6):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=3 and column <= 9:
        for r in range(3):
            for c in range(6, 9):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=6 and column <= 3:
        for r in range(3, 6):
            for c in range(3):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=6 and column <= 6:
        for r in range(3, 6):
            for c in range(3, 6):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=6 and column <= 9:
        for r in range(3, 6):
            for c in range(6, 9):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=9 and column <= 3:
        for r in range(6, 9):
            for c in range(3):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=9 and column <= 6:
        for r in range(6, 9):
            for c in range(3, 6):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    elif row <=9 and column <= 9:
        for r in range(6, 9):
            for c in range(6, 9):
                if puzzle_grid[r][c] != 0:
                    box.append(puzzle_grid[r][c])
    return box

def return_col(puzzle_grid, column):
    col = []
    for r in range(9):
        if puzzle_grid[r][column-1] != 0:
            col.append(puzzle_grid[r][column-1])
    return col
                    

def find_possible_values(puzzle_grid, row, col, box):
    possibilities = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    
    for ele in range(1, 10):
        if (ele in puzzle_grid[row-1]) or (ele in col) or (ele in box):
            possibilities.remove(ele)

    return possibilities


def print_output(puzzle_grid):
    with open("puzzle_output.txt", "w") as out:
        for r in range(9):
            print(puzzle_grid[r], file = out)

def puz_procedure(puzzle_grid):
    puz_check = []
    puz_ret_val = []
    while check_puzzle_full(puzzle_grid) == 0:
        puz_check = copy.deepcopy(puzzle_grid)
        ret_val = basic_solver(puzzle_grid)
        if ret_val == 1:
            puz_ret_val = [puzzle_grid,1]
            return puz_ret_val
        for r in range(1, 9, 3):
            for c in range(1, 9, 3):
                puz_ret_val = copy.deepcopy(box_solver(puzzle_grid,r,c))
                puzzle_grid = copy.deepcopy(puz_ret_val[0])
                ret_val = puz_ret_val[1]
    
        for r in range(1,10):
            puz_ret_val = copy.deepcopy(column_solver(puzzle_grid,r))
            puzzle_grid = copy.deepcopy(puz_ret_val[0])
            ret_val = puz_ret_val[1]
            
        for c in range(1,10):
            puz_ret_val = copy.deepcopy(row_solver(puzzle_grid,c))
            puzzle_grid = copy.deepcopy(puz_ret_val[0])
            ret_val = puz_ret_val[1]

        if check_puzzle_full(puzzle_grid) == 1:
            puz_ret_val = [puzzle_grid,1]
            return puz_ret_val
        if puz_check == puzzle_grid:
            puz_ret_val = [puzzle_grid,0]
            return puz_ret_val
        
            


def basic_solver(puzzle_grid):
    puzzle_check = []
    while check_puzzle_full(puzzle_grid) == 0:
        puzzle_check = copy.deepcopy(puzzle_grid)
        for r in range(1, 10):
            for c in range(1, 10):
                if puzzle_grid[r-1][c-1] == 0:
                    box = return_box(puzzle_grid, r, c)
                    col = return_col(puzzle_grid, c)
                    poss_outcome = find_possible_values(puzzle_grid,r,col,box).copy()
                    if len(poss_outcome) == 1:
                        puzzle_grid[r-1][c-1] = poss_outcome[0]
        if puzzle_check == puzzle_grid:
            return 0
    return 1

def checker(puzzle_grid, poss_r_c):
    success = 0
    grid_suc = []
    loc = 0
    for element in range(1, 10):
        count = 0
        for number in range(len(poss_r_c)):
            if element in poss_r_c[number][0]:
                count += 1
                loc = number
        if count == 1:
            puzzle_grid[poss_r_c[loc][1] - 1][poss_r_c[loc][2] - 1] = element  
            success = 1
            break
    grid_suc = [puzzle_grid,success]
    return grid_suc

def box_solver(puzzle_grid,r,c):
    poss_r_c = []
    grid_ret_val = []
    for row in range(r, r+3):
        for column in range(c, c+3):
            if puzzle_grid[row-1][column-1] == 0:
                box = return_box(puzzle_grid, row, column)
                col = return_col(puzzle_grid, column)
                poss_outcome = find_possible_values(puzzle_grid,row,col,box).copy()   
                poss_r_c.append([poss_outcome,row,column])
    grid_ret_val = copy.deepcopy(checker(puzzle_grid, poss_r_c))
    ret_val = grid_ret_val[1]
    if ret_val == 1:
        ret_val = basic_solver(grid_ret_val[0])
        grid_ret_val[1] = ret_val 
    return grid_ret_val

def column_solver(puzzle_grid,r):
    poss_r_c = []
    grid_ret_val = []
    for column in range(1, 10):
        if puzzle_grid[r-1][column-1] == 0:
            box = return_box(puzzle_grid, r, column)
            col = return_col(puzzle_grid, column)
            poss_outcome = find_possible_values(puzzle_grid,r,col,box).copy()   
            poss_r_c.append([poss_outcome,r,column])
    grid_ret_val = copy.deepcopy(checker(puzzle_grid, poss_r_c))
    ret_val = grid_ret_val[1]
    if ret_val == 1:
        ret_val = basic_solver(grid_ret_val[0])
        grid_ret_val[1] = ret_val 
    return grid_ret_val

def row_solver(puzzle_grid,c):
    poss_r_c = []
    grid_ret_val = []
    for row in range(1, 10):
        if puzzle_grid[row-1][c-1] == 0:
            box = return_box(puzzle_grid, row, c)
            col = return_col(puzzle_grid, c)
            poss_outcome = find_possible_values(puzzle_grid,row,col,box).copy()   
            poss_r_c.append([poss_outcome,row,c])
    grid_ret_val = copy.deepcopy(checker(puzzle_grid, poss_r_c))
    ret_val = grid_ret_val[1]
    if ret_val == 1:
        ret_val = basic_solver(grid_ret_val[0])
        grid_ret_val[1] = ret_val 
    return grid_ret_val


                
def assume_solver(puzzle_grid,possibilities,r,c):
    puz_dup = copy.deepcopy(puzzle_grid)
    for element in possibilities:
        puzzle_grid[r-1][c-1] = element
        puz_ret_val = copy.deepcopy(puz_procedure(puzzle_grid))
        ret_val = puz_ret_val[1]
        if ret_val == 1:
            return puz_ret_val
    puz_ret_val = [puz_dup,0]
    return puz_ret_val