import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { getSeek, postSeek, postAccept, getGame, deleteSeek } from '../../rest/rest.js';

import Player from '../player/Player.js';
import Seeks from '../seeks/Seeks.js';
import Board from '../board/Board.js';

import stompClient from '../../websocket.js';

import './app.css';

class App extends Component {

	constructor(props) {
		super(props);
		this.state = {currentPlayer: null, game: null, alertMessage: ''};

		this.onPlayerCreated = this.onPlayerCreated.bind(this);
		this.onGameCreated = this.onGameCreated.bind(this);
		this.onSeekAccepted = this.onSeekAccepted.bind(this);
		this.onSeekSubmitted = this.onSeekSubmitted.bind(this);
		this.acceptedSeek = this.acceptedSeek.bind(this);
		this.quitGame = this.quitGame.bind(this);
		this.gameOver = this.gameOver.bind(this);
		this.alert = this.alert.bind(this);
	}

	componentDidMount() {
		stompClient.register([
			{route: '/topic/newAccept', callback: this.acceptedSeek},
		]);
	}

	acceptedSeek(response) {
		if (this.state.game === null && this.state.currentPlayer !== null) {
			const seekId = parseInt(response.body, 10);
			getSeek(response => {
				let seek = response.entity;
				if (seek.seeker.id === this.state.currentPlayer.id) {
					this.setState({game: seek.game});
				}
			}, seekId);
		}

	}

	onPlayerCreated(currentPlayer) {
		this.alert("Created a player!");
		this.setState({currentPlayer});
	}

	onSeekSubmitted(value) {
		this.alert('Your seek for a ' + value + ' card game has been submitted.');
		this.submitSeek(value);
	}

	onSeekAccepted(seekId) {
		this.acceptSeek(this.state.currentPlayer.id, seekId);
	}

	submitSeek(cardPairCount) {
		let playerId = this.state.currentPlayer.id;
		postSeek(response => {
			if (response.entity.error) {
				this.alert(response.entity.error);
			}
		}, playerId, cardPairCount);
	}

	acceptSeek(playerId, id) {
		postAccept(response => {
			if (response.entity.error) {
				this.alert(response.entity.error);
			}
			else {
				const game = response.entity;
				this.setState({game});
				deleteSeek(() => {}, id);
			}
		}, id, playerId);
	}

	onGameCreated(game) {
		this.setState({game});
	}

	alert(message) {
		this.timeout | clearTimeout(this.timeout);
		this.setState({alertMessage: message});
		this.timeout = setTimeout(() => {
			this.setState({alertMessage: ''});
		}, 2500);
	}

	gameOver() {		
		getGame( response => {
			this.setState({game: response.entity});
		}, this.state.game.id);
	}

	quitGame() {
		this.setState({game: null});
	}

	render() {

		let showPlayerComponent = false;
		let showSeeksComponent = false;
		let showGameComponent = false;
		if (this.state.currentPlayer === null) {
			showPlayerComponent = true;
		}
		else if (this.state.game === null) {
			showSeeksComponent = true;
		}
		else {
			showGameComponent = true;
		}		
		
		return (
			<div>

				<div id='alertMsg'>{this.state.alertMessage}</div>

				{
					showPlayerComponent ? (
						<div>
							<Player 
								onCreate={this.onPlayerCreated}
							/>
						</div>
					) : ''
				}

				{
					showSeeksComponent ? (
						<div>
							<h2>Welcome {this.state.currentPlayer.name}!</h2>
							<Seeks 
								onSeekSubmitted={this.onSeekSubmitted} 
								onSeekAccepted={this.onSeekAccepted} 
								playerId={this.state.currentPlayer.id} 
							/>
						</div>
					) : ''
				}

				{
					showGameComponent ? (
						<Board 
							gameId={this.state.game.id} 
							playerId={this.state.currentPlayer.id} 
							alert={this.alert} 
							onQuit={this.quitGame}
							onGameOver={this.gameOver}
						/>
					) : ''
				}
				
			</div>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
