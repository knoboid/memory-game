import React, { Component } from 'react';

import { getPlayersByName, postPlayer } from '../../rest/rest.js';

class Player extends Component {

	constructor(props) {
		super(props);
		this.state = {players: []};
		this.handleNameSubmit = this.handleNameSubmit.bind(this);
	}

	handleNameSubmit(e) {
		if (e.key === "Enter") {
			const name = e.target.value.trim();
			// First check if the inputted player exists - then take it over!
			getPlayersByName(response => {
				const players = response.entity;
				if (players.length > 0) {
					this.props.onCreate(players[0]);
				}
				else {
					postPlayer((response) => {
						this.props.onCreate(response.entity);
					}, name);
				}
			}, name);
		}
	}

	render() {
		return (
			<div>
				<div>
					Enter your name:
				</div>
				<br />
				<input
					className="input"
					type="text"
					style={{width:'15em'}}
					onKeyDown={this.handleNameSubmit}
					disabled={false}
				/>
			</div>
		)
	}
}

export default Player;