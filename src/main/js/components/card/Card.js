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
		return (
            <span className='card'>
                (<span href='#' onClick={this.handleCardTurn}>{this.props.card}</span>)
            </span>
		)
	}
}

export default Card;