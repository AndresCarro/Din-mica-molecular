import csv
import pandas as pd
import numpy as np
import json
import sys
import math
import cv2
import matplotlib.pyplot as plt


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/'
DEFAULT_INPUT_PATH = '../../Simulation/input/'
AVG_PATH = '../output/'
N = 250
FAKE_L = 0
L = 0.1
# ---------------------------------------------------

# ---------------------------------------------------
# CONSTANTES
# ---------------------------------------------------
DELTA_T = 0.01
CIRCLE_RADIUS = 0.005
MASS = 1
NON_WALL_OBJECTS = ['INIT', 'CENTER', 'NONE']
# ---------------------------------------------------


class CrashSpeed:
    def __init__(self, speed_before, angle, wall):
        self.v_o = speed_before
        self.angle = angle
        self.wall = wall

    def calculate_delta_normal_speed(self):
        if self.wall == 'UP' or self.wall == 'DOWN':
            return abs(- (self.v_o * np.sin(self.angle)) - (self.v_o * np.sin(self.angle)))
        elif self.wall == 'LEFT' or self.wall == 'RIGHT':
            return abs(- (self.v_o * np.cos(self.angle)) - (self.v_o * np.cos(self.angle)))

class ObstacleCrashSpeed:
    def __init__(self, velocity, angle, x, y):
        self.v = velocity
        self.angle = angle
        self.x = x
        self.y = y

    def calculate_delta_normal_speed(self):
        alpha = math.atan2(self.y - 0.05, self.x - 0.05)
        return abs((self.v * math.cos(alpha)) * 2)


def calculate_pressure_values(particles_coords):
    time_frames = particles_coords['time'].unique()
    wall_pressure_values = []
    obstacle_pressure_values = []
    current_time = 0
    delta_t_limit = 0
    current_wall_velocities = []
    current_obstacle_velocities = []
    count_wall_crashes = 0

    for time_frame in time_frames:
        current_particle_coords = particles_coords[particles_coords['time'] == time_frame]
        current_time = time_frame

        if current_time - delta_t_limit > DELTA_T:
            wall_pressure_values.append(sum(current_wall_velocities))
            current_wall_velocities = []
            obstacle_pressure_values.append(sum(current_obstacle_velocities))
            current_obstacle_velocities = []
            delta_t_limit += DELTA_T

        # Save delta_v values for particles that have crashed into a wall
        for index, fila in current_particle_coords.iterrows():
            if fila['crash'] not in NON_WALL_OBJECTS and fila['id'] == fila['ParticleA']:
                current_wall_velocities.append(CrashSpeed(fila['vel'], fila['angulo'],
                                                          fila['crash']).calculate_delta_normal_speed() / (DELTA_T * 4 * L))
                break
            elif fila['crash'] == 'CENTER' and fila['id'] == fila['ParticleA']:
                current_obstacle_velocities.append(ObstacleCrashSpeed(fila['vel'], fila['angulo'], fila['x'],
                                                                      fila['y']).calculate_delta_normal_speed() / (DELTA_T * 2 * math.pi * CIRCLE_RADIUS))
                break

    return wall_pressure_values, obstacle_pressure_values



def calculate_p_vs_t(speeds):
    graph_wall_pressure_values = []
    graph_std_wall_graph_values = []
    graph_obstacle_pressure_values = []
    graph_std_obstacle_pressure_values = []

    for speed in speeds:
        particles_coords = pd.read_csv(OUTPUT_PATH + 'SimulationData_' + str(N) + '_' + str(FAKE_L)
                                       + "_" + str(speed) + '.csv')
        wall_pressure_values, obstacle_pressure_values = calculate_pressure_values(particles_coords)
        std_wall_graph_values = np.std(wall_pressure_values)
        std_obstacle_graph_values = np.std(obstacle_pressure_values)
        std_obstacle_graph_values /= 2
        std_wall_graph_values /= 2
        graph_wall_pressure_values.append(sum(wall_pressure_values) / len(wall_pressure_values))
        graph_obstacle_pressure_values.append(sum(obstacle_pressure_values) / len(obstacle_pressure_values))
        graph_std_wall_graph_values.append(std_wall_graph_values)
        graph_std_obstacle_pressure_values.append(std_obstacle_graph_values)

    return graph_wall_pressure_values, graph_std_wall_graph_values, graph_obstacle_pressure_values, graph_std_obstacle_pressure_values


def main():
    speeds = [1, 3, 6, 10]
    temperature_values = [speed**2 for speed in speeds]
    (graph_wall_pressure_values, graph_std_wall_graph_values, graph_obstacle_pressure_values,
     graph_std_obstacle_pressure_values) = calculate_p_vs_t(speeds)

    plt.figure(figsize=(10, 6))

    plt.errorbar(temperature_values, graph_wall_pressure_values, yerr=graph_std_wall_graph_values, fmt='o')
    plt.errorbar(temperature_values, graph_obstacle_pressure_values, yerr=graph_std_obstacle_pressure_values, fmt='o')
    plt.xlabel('Temperatura (U.A)', fontsize=16)
    plt.ylabel('Presión (Pa)', fontsize=16)
    plt.grid(False)

    plt.show()


if __name__ == '__main__':
    main()
