import argparse
import datetime
import os
from nats.aio.client import Client as NATS

def process_message(msg):
    symbol, adjustment, price = msg.data.decode().split(',')

    timestamp = datetime.datetime.now().isoformat()
    
    log_file = f'{symbol}_log.txt'
    with open(log_file, 'a') as file:
        file.write(f'{timestamp}, {adjustment}, {price}\n')

    print(f'Received message: ')
async def run_monitor(nats_url, symbols):
    nc = NATS()
    await nc.connect(servers=[nats_url])

    for symbol in symbols:
        await nc.subscribe(symbol, cb=process_message)
        
    while True:
        await asyncio.sleep(1)
    
if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='StockMonitor application')
    parser.add_argument('nats_url', help='NATS server URL')
    parser.add_argument('symbols', nargs='*', help='Stock symbols to track')
    args = parser.parse_args()

    if not os.path.exists('logs'):
        os.makedirs('logs')

    os.chdir('logs')

    asyncio.run(run_monitor(args.nats_url, args.symbols))

