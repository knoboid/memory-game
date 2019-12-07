import React, { Component } from 'react';

import Card from '../card/Card.js';
import { getGame, postMove, completeTurn } from '../../rest/rest.js';

import stompClient from '../../websocket.js';

class Board extends Component {

	constructor(props) {
		super(props);
        this.cardClicked = this.cardClicked.bind(this);
        this.state = {game: {board: []}, loaded: false};
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
		
		const style = {float: 'left'};
		
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
								<div key={i} style={ (i % 3 === 0) ? {float: 'left', clear: 'left'} : {float: 'left'}}>
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