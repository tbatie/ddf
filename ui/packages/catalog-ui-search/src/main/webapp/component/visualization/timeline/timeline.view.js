const CustomElements = require('../../../js/CustomElements.js')
const Marionette = require('marionette')
import React from 'react'
import TimelineVisualization from './timeline'

module.exports = Marionette.ItemView.extend({
  tagName: CustomElements.register('testing-timeline'),

  template(props) {
    return (
      <React.Fragment>
        <TimelineVisualization
          selectionInterface={this.options.selectionInterface}
        />
      </React.Fragment>
    )
  },
})
