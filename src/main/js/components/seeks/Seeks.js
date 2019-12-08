import React, { Component } from 'react';

import Seek from '../seek/Seek.js';
import { getSeeks, getSeeksBySeekerId } from '../../rest/rest.js';
import stompClient from '../../websocket.js';

class Seeks extends Component {

	constructor(props) {
		super(props);
		this.state = {seeks: []};
		this.onSeekAccepted = this.onSeekAccepted.bind(this);
		this.newSeeks = this.newSeeks.bind(this);
		this.handleSeekSubmit = this.handleSeekSubmit.bind(this);
	}

	componentDidMount() {
		this.loadSeeks();
		stompClient.register([
			{route: '/topic/newSeek', callback: this.newSeeks},
		]);
	}

	newSeeks() {
		getSeeksBySeekerId(response => {
			const seeks = response.entity;
		}, this.props.playerId);

		this.loadSeeks();
	}

	loadSeeks() {
		getSeeks(response => {
			this.setState({seeks: response.entity});
		});
	}

    onSeekAccepted(seekId) {
		this.props.onSeekAccepted(seekId);
    }

	handleSeekSubmit(e) {
		let value = 5;
		if (e.key === "Enter") {
			value = e.target.value;
			this.props.onSeekSubmitted(value);
		}
	}

	render() {
		return (
			<div>
				Create a new seek with 
				<input
					className="input"
					type="text"
					style={{width:'3em'}}
					onKeyDown={this.handleSeekSubmit}
					defaultValue={5}
					disabled={false}
				/> card pairs.
				<div>
					Current Seeks:
                    {
                        this.state.seeks.map((seek, i) => {
							const game = seek.game;							
                            return (
								<div key={i}>
									<Seek 
										name={game.player1.name} 
										cards={game.cardPairCount} 
										seekId={seek.id} 
										onSeekAccepted={this.onSeekAccepted}
									/>
                                </div>
                            )
                        })
                    }
				</div>

			</div>
		)
	}
}

export default Seeks;