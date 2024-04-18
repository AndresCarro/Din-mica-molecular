import csv
import pandas as pd
import numpy as np
import json
import sys
import cv2
import matplotlib.pyplot as plt
from pressure_vs_temperature import CrashSpeed


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/'
DEFAULT_INPUT_PATH = '../../Simulation/input/'
AVG_PATH = '../output/'
FAKE_L = 0
N = 250
SPEED = 1
# ---------------------------------------------------

# ---------------------------------------------------
# CONSTANTES
# ---------------------------------------------------
DELTA_T = 1.820695 / 200
L = 0.01
MASS = 1
NON_WALL_OBJECTS = ['INIT', 'CENTER', 'NONE']
# ---------------------------------------------------


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
            count_wall_crashes = len(current_wall_velocities)
            print('Amount of wall crashes in this delta:', count_wall_crashes)
            current_wall_velocities = []
            delta_t_limit += DELTA_T

        # Save delta_v values for particles that have crashed into a wall
        for index, fila in current_particle_coords.iterrows():
            if fila['crash'] not in NON_WALL_OBJECTS and fila['id'] == fila['ParticleA']:
                current_wall_velocities.append(CrashSpeed(fila['vel'], fila['angulo'],
                                                     fila['crash']).calculate_delta_normal_speed() / (DELTA_T * 4 * L))
                break
            elif fila['crash'] == 'CENTER':
                pass

    return wall_pressure_values


def main():
    particles_coords = pd.read_csv(OUTPUT_PATH + 'SimulationData_' + str(N) + '_' + str(FAKE_L)
                                   + "_" + str(SPEED) + '.csv')
    pressure_values = calculate_pressure_values(particles_coords)
    print('Pressure values: ', pressure_values)
    time_values = []

    for index in range(len(pressure_values)):
        time_values.append(index * DELTA_T)

    fig, ax = plt.subplots()
    ax.plot(time_values, pressure_values)

    ax.set_xlabel('Tiempo [s]')
    ax.set_ylabel('Presión')
    ax.set_title('')

    # Step 6: Show or save the graph
    plt.show()


if __name__ == '__main__':
    main()
