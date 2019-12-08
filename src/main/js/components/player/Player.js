const React = require('react');
const client = require('../../client');

import { getPlayersByName } from '../../rest/rest.js';

class Player extends React.Component {

	constructor(props) {
		super(props);
		this.state = {players: []};
		this.handleNameSubmit = this.handleNameSubmit.bind(this);
	}

	componentDidMount() {
		this.getPlayers();
	}

	getPlayers() {
		client({method: 'GET', path: '/api/players'}).done(response => {
			this.setState({players: response.entity});
		});
	}

	postUser(name) {
		client({method: 'POST', path: '/api/player', params: {name: name}}).done(response => {
			this.props.onCreate(Object.assign({}, response.entity));
		});
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
					this.postUser(name);
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