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
L = 10
RADIUS_PARTICLES = 0.001
N = 20
RADIUS_CIRCLE = 0.005
# ---------------------------------------------------

PARTICLES_COORDINATES_FILE2 = ('../../Simulation/output/SimulationData_' + str(N) + '_' + str(L) + '.csv')
CONFIG_FILE = '../../Simulation/input/input.json'
OPENCV_OUTPUT_FILENAME = '../output/open_cv_output'
MP4_FORMAT = 'mp4'
SCALE_FACTOR = 150
WALL_COLOR = (255, 255, 255)
WALL_THICKNESS = 200
PARTICLE_COLOR = (255, 0, 0)


def complete_visualization_opencv(particles_coords, timeFrames, particle_radius, L, N):
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    video_writer = cv2.VideoWriter(OPENCV_OUTPUT_FILENAME + '.' + MP4_FORMAT, fourcc, 15.0, (L*SCALE_FACTOR,
                                                                                             L*SCALE_FACTOR))
    for timeFrame in timeFrames:
        current_particle_coords = particles_coords[particles_coords['time'] == timeFrame]
        frame = np.full((L*SCALE_FACTOR, L*SCALE_FACTOR, 3), 255, dtype=np.uint8)

        # Draw walls
        cv2.line(frame, (0, 0), (L * SCALE_FACTOR, 0), WALL_COLOR, WALL_THICKNESS)
        cv2.line(frame, (0, 0), (0, L * SCALE_FACTOR), WALL_COLOR, WALL_THICKNESS)
        cv2.line(frame, (L * SCALE_FACTOR, 0), (L-1, L * SCALE_FACTOR), WALL_COLOR, WALL_THICKNESS)
        cv2.line(frame, (0, L * SCALE_FACTOR), (L * SCALE_FACTOR, L * SCALE_FACTOR), WALL_COLOR, WALL_THICKNESS)


        for index, fila in current_particle_coords.iterrows():
            dx = np.cos(fila['angulo']) * fila['vel'] * SCALE_FACTOR
            dy = np.sin(fila['angulo']) * fila['vel'] * SCALE_FACTOR
            start_pos = [int(fila['x'] * SCALE_FACTOR), int(fila['y'] * SCALE_FACTOR)]
            finish_pos = [int(SCALE_FACTOR * fila['x']) + int(dx), int(SCALE_FACTOR * fila['y']) + int(dy)]
            current_pos = [int(fila['x'] * SCALE_FACTOR), int(fila['y'] * SCALE_FACTOR)]

            cv2.arrowedLine(frame, tuple(start_pos), tuple(finish_pos),
                            PARTICLE_COLOR, 5, cv2.LINE_AA)
            cv2.circle(frame, tuple(current_pos), int(particle_radius * SCALE_FACTOR), PARTICLE_COLOR, -1)


        video_writer.write(frame)
    video_writer.release()
    cv2.destroyAllWindows()


def read_config_file(file_path):
    with open(file_path, 'r') as file:
        config_data = json.load(file)
    return config_data


if __name__ == '__main__':
    # config = read_config_file(OUTPUT_PATH + 'StateData_' + str(N) + '_' + str(L) + '.json')
    config = read_config_file(DEFAULT_INPUT_PATH + 'input.json')
    particles_coords = pd.read_csv(PARTICLES_COORDINATES_FILE2)
    circles = []
    circles_coords = []

    timeFrames = particles_coords['time'].unique()
    particle_radius = config['radius']

    # OpenCV #
    print('Drawing particles with opencv...')
    complete_visualization_opencv(particles_coords, timeFrames, particle_radius, L, N)
    print('DONE!')
