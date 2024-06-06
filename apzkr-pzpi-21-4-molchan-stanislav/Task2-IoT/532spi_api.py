import json
import board
import busio
import digitalio
from adafruit_pn532.spi import PN532_SPI
import requests
import time

with open('common/api_routes.json', 'r') as file:
    routes = json.load(file)

CHECK_EXIST_URL = routes['CHECK_EXIST_URL']
CREATE_TAG_URL = routes['CREATE_TAG_URL']
UPDATE_TAG_URL = routes['UPDATE_TAG_URL']

spi = busio.SPI(board.SCK, board.MOSI, board.MISO)
cs_pin = digitalio.DigitalInOut(board.D7)
pn532 = PN532_SPI(spi, cs_pin, debug=False)

def format_uid(uid):
    formatted_uid = ""
    checksum = 0
    for byte in uid:
        high_nibble = (byte >> 4) & 0xF
        low_nibble = byte & 0xF
        high_char = chr(high_nibble + 48) if high_nibble < 10 else chr(high_nibble + 55)
        low_char = chr(low_nibble + 48) if low_nibble < 10 else chr(low_nibble + 55)
        formatted_uid += high_char + low_char
        checksum += byte
    checksum_hex = hex(checksum % 256)[2:].zfill(2)
    formatted_uid += '-' + checksum_hex
    return formatted_uid

pn532.SAM_configuration()

print("Waiting for an RFID/NFC card...")
while True:
    uid = pn532.read_passive_target(timeout=0.5)
    if uid is not None:
        uid_str = format_uid(uid)
        print('Found card with UID:', uid_str)
        try:
            check_response = requests.get(CHECK_EXIST_URL.format(tagId=uid_str))
            if check_response.status_code == 200 and check_response.json():
                print("Tag already exists. Updating status...")
                update_response = requests.put(UPDATE_TAG_URL.format(tagId=uid_str))
                if update_response.status_code == 200:
                    print("Tag status updated successfully.")
                else:
                    print("Failed to update tag status. Status code:", update_response.status_code)
            else:
                print("Tag does not exist. Creating...")
                create_response = requests.post(CREATE_TAG_URL, json={"tagId": uid_str})
                if create_response.status_code == 200:
                    print("Tag created successfully.")
                else:
                    print("Failed to create tag. Status code:", create_response.status_code)
            read_data = pn532.ntag2xx_read_block(4)
            print("Data read from the tag:", read_data)
            time.sleep(5)
        except Exception as e:
            print("Error:", e)
