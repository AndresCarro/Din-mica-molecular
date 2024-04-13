import csv
import pandas as pd
import numpy as np
import json
import sys
import cv2
import matplotlib.pyplot as plt


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/'
DEFAULT_INPUT_PATH = '../../Simulation/input/'
AVG_PATH = '../output/'
N = 250
L = 0
# ---------------------------------------------------

# ---------------------------------------------------
# CONSTANTES
# ---------------------------------------------------
DELTA_T = 0.001
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


def calculate_pressure_values(particles_coords):
    time_frames = particles_coords['time'].unique()
    pressure_values = []
    current_time = 0
    delta_t_limit = 0
    current_velocities = []

    for time_frame in time_frames:
        current_particle_coords = particles_coords[particles_coords['time'] == time_frame]
        current_time = time_frame

        if current_time - delta_t_limit > DELTA_T:
            pressure_values.append(sum(current_velocities))
            current_velocities = []
            delta_t_limit += DELTA_T

        # Save delta_v values for particles that have crashed into a wall
        for index, fila in current_particle_coords.iterrows():
            if fila['crash'] not in NON_WALL_OBJECTS and fila['id'] == fila['ParticleA']:
                current_time += fila['time']
                current_velocities.append(CrashSpeed(fila['vel'], fila['angulo'],
                                                     fila['crash']).calculate_delta_normal_speed() / DELTA_T)
                break

    return pressure_values


def calculate_p_vs_t(speeds):
    graph_pressure_values = []

    for speed in speeds:
        particles_coords = pd.read_csv(OUTPUT_PATH + 'SimulationData_' + str(N) + '_' + str(L)
                                       + "_" + str(speed) + '.csv')
        graph_pressure_values = sum(calculate_pressure_values(particles_coords))

    return graph_pressure_values


def main():
    speeds = [1, 3, 6, 10]
    temperature_values = [speed**2 for speed in speeds]
    pressure_values = calculate_p_vs_t(speeds)

    # Step 3: Create a figure and axis object
    fig, ax = plt.subplots()

    # Step 4: Plot your data
    ax.plot(temperature_values, pressure_values)

    # Step 5: Customize your graph
    ax.set_xlabel('Temperatura (v²)')
    ax.set_ylabel('Presión')
    ax.set_title('Conchonaro')

    # Step 6: Show or save the graph
    plt.show()


if __name__ == '__main__':
    main()
