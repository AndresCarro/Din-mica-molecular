import csv
import pandas as pd
import numpy as np
import json
import sys
import cv2

# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/'
DEFAULT_INPUT_PATH = '../../Simulation/input/'
AVG_PATH = '../output/'
L = 0.1
FAKE_L = 0
RADIUS_PARTICLES = 0.001
N = 250
SPEED = 2
RADIUS_CIRCLE = 0.005
# ---------------------------------------------------

PARTICLES_COORDINATES_FILE2 = ('../../Simulation/output/SimulationData_' + str(N) + '_' + str(FAKE_L) +
                               '_' + str(SPEED) + '.csv')
CONFIG_FILE = '../../Simulation/input/input.json'
OPENCV_OUTPUT_FILENAME = '../output/open_cv_output'
MP4_FORMAT = 'mp4'
SCALE_FACTOR = 10000
WALL_COLOR = (255, 255, 255)
WALL_THICKNESS = 200
PARTICLE_COLOR = (255, 0, 0)
CIRCLE_COLOR = (133, 133, 133)
INFECTED_COLOR = (0, 0, 255)


def complete_visualization_opencv(particles_coords, timeFrames, particle_radius, L, circle_radius, is_movable,
                                  visualization_mode):
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    SCALED_L = int(L * SCALE_FACTOR)
    video_writer = cv2.VideoWriter(OPENCV_OUTPUT_FILENAME + '.' + MP4_FORMAT, fourcc, 60.0, (SCALED_L,
                                                                                             SCALED_L))
    infected_map = {}
    for timeFrame in timeFrames:
        current_particle_coords = particles_coords[particles_coords['time'] == timeFrame]
        frame = np.full((SCALED_L, SCALED_L, 3), 255, dtype=np.uint8)

        if is_movable is False:
            # Draw middle circle
            cv2.circle(frame, tuple([int(SCALED_L / 2), int(SCALED_L / 2)]), int(circle_radius * SCALE_FACTOR),
                       CIRCLE_COLOR, -1)

        for index, fila in current_particle_coords.iterrows():
            current_pos = [int(fila['x'] * SCALE_FACTOR), int(fila['y'] * SCALE_FACTOR)]
            if is_movable and fila['id'] == N:  # particle id == moving obstacle id
                cv2.circle(frame, tuple(current_pos), int(circle_radius * SCALE_FACTOR), CIRCLE_COLOR, -1)
            elif visualization_mode != 'infected':
                cv2.circle(frame, tuple(current_pos), int(particle_radius * SCALE_FACTOR), PARTICLE_COLOR, -1)
            else:  # visualization_mode == 'infected'
                if (fila['crash'] == 'NONE' and (fila['ParticleA'] == N or fila['ParticleB'] == N) and
                        fila['id'] != N and fila['id'] in [fila['ParticleA'], fila['ParticleB']]):  # Has gotten infected
                    # if (fila['crash'] == 'CENTER' and fila['id'] in [fila['ParticleA'], fila['ParticleB']]):  # Has gotten infected
                    infected_map[fila['id']] = 1
                    particle_color = INFECTED_COLOR
                elif infected_map.get(fila['id']) == 1:  # is already infected
                    particle_color = INFECTED_COLOR
                else:  # not infected
                    particle_color = PARTICLE_COLOR
                cv2.circle(frame, tuple(current_pos), int(particle_radius * SCALE_FACTOR), particle_color, -1)

        video_writer.write(frame)

    video_writer.release()
    cv2.destroyAllWindows()


def read_config_file(file_path):
    with open(file_path, 'r') as file:
        config_data = json.load(file)
    return config_data


if __name__ == '__main__':
    config = read_config_file(OUTPUT_PATH + 'StateData_' + str(N) + '_' + str(FAKE_L) + '_' + str(SPEED) + '.json')
    particles_coords = pd.read_csv(PARTICLES_COORDINATES_FILE2)
    visualization_mode = sys.argv[1]

    timeFrames = particles_coords['time'].unique()
    particle_radius = RADIUS_PARTICLES

    # OpenCV #
    print('Drawing particles with opencv...')
    complete_visualization_opencv(particles_coords, timeFrames, particle_radius, L, RADIUS_CIRCLE, config['movable'],
                                  visualization_mode)
    print('DONE!')
