const React = require('react');

import './card.css';

class Card extends React.Component {

	constructor(props) {
        super(props);
        this.handleCardTurn = this.handleCardTurn.bind(this); 
    }

    handleCardTurn() {
        this.props.onClick(this.props.index);
    }

	render() {
        const isFaceDown = (this.props.card == 0);
        const cardStyle = isFaceDown ? 'faceDown' : 'faceUp';
        const cardText = isFaceDown ? '' : this.props.card;
        const cardColour = isFaceDown ? '#553000' : '#fff8ce';
        
		return (
            <span className='container' onClick={this.handleCardTurn}>
                <svg className='backgroundCircle' height="100" width="100">
                    <circle cx="45" cy="45" r="40" stroke="black" strokeWidth="3" fill={cardColour} />
                    hello
                </svg> 
                <span className={'cardText ' + cardStyle} href='#'>{cardText}</span>
            </span>
		)
	}
}

export default Card;