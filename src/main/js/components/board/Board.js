import React, { Component } from 'react';

import Card from '../card/Card.js';
import { getGame, postMove, completeTurn } from '../../rest/rest.js';

import stompClient from '../../websocket.js';

import './board.css';

class Board extends Component {

	constructor(props) {
		super(props);
        this.state = {game: {board: []}, loaded: false, gameOver: false};
        this.cardClicked = this.cardClicked.bind(this);
        this.turnCard = this.turnCard.bind(this);
        this.cardClicked = this.cardClicked.bind(this);
		this.newMove = this.newMove.bind(this);
		this.quit = this.quit.bind(this);
	}

	componentDidMount() {        
		this.loadGame();
		stompClient.register([
			{route: '/topic/newMove', callback: this.newMove},
		]);
	}

	componentDidUpdate() {
		if (!this.state.gameOver && this.state.game.gameOver) {			
			this.props.onGameOver();
			this.setState({gate: false, gameOver: true});
		}
	}

	newMove(response) {
		const gameId = parseInt(response.body, 10);
		if (gameId === this.state.game.id) {
			this.loadGame();
		}
	}

	loadGame() {
		getGame(response => {
			const game = response.entity;
			this.setState({ game: game, loaded: true });
		}, this.props.gameId);
	}

	cardClicked(index) {
        this.turnCard(index);
    }

    turnCard(cardIndex) {
        const { playerId, gameId } = this.props;
        postMove( (response)  => {
            if (response.entity.error) {
                this.props.alert(response.entity.error);
			}
			else {
				const game = response.entity;
				const delay = game.successfulTurn ? 0 : 2500;
				if (game.lock) {
					setTimeout(() => {
						completeTurn(() => {}, this.props.gameId);
					}, delay);
				}
			} 
        }, gameId, playerId, cardIndex);
	}

	quit() {
		this.props.onQuit();
	}

	render() {
        const { game } = this.state;
		const columns = Math.floor(Math.sqrt(game.cardPairCount * 2));		

		return (
			<div>
				{
					this.state.loaded ? (
						<div>
							<h4>Score</h4>
							<div>{game.player1.name} ({game.score1} - {game.score2}) {game.player2.name}</div>
						</div>

					) : ''
				}
                <div>
                    {
                        game.board.map((card, i) => {
                            return (
								<div className='cardDiv' key={i} style={ (i % columns === 0) ? {clear: 'left'} : {}}>
                                    <Card index={i} card={card} onClick={this.cardClicked}/>
                                </div>
                            )
                        })
					}
				</div>
				<button onClick={this.quit} style={{float: 'left', clear: 'left'}}>Quit</button>
			</div>
		)
	}
}

export default Board;
