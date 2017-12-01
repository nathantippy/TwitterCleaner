import React, { Component } from 'react';
import { Icon } from 'semantic-ui-react';
import { Link } from 'react-router-dom';

import Modal from './Modal';
import styles from '../scss/SettingsPage.module.scss';

export default class SettingsPage extends Component {
  state = {
    activeModal: null,
    languages: [
      'English',
      'Spanish',
      'Italian',
      'French',
      'Mandarin',
      'Cantonese',
      'Japanese'
    ],
    regions: [
      'North America',
      'South America',
      'Europe',
      'Asia',
      'Africa',
      'Australia'
    ],
    nsfws: [
      'Swearing',
      'Nudity',
      'Pornography',
      'Sexual Content (Talking about sex)',
      'Gross'
    ],
    misc: ['Book seller', 'Politics']
  };

  openModal(modal) {
    this.setState({ activeModal: modal });
  }

  closeModal() {
    this.setState({ activeModal: null });
  }

  render() {
    return (
      <div className={styles.container}>
        <Link to="/" className={styles.backButton}>
          <Icon name="chevron left" size="large" />
          <span>Back</span>
        </Link>
        <Modal
          isOpen={this.state.activeModal === 'region'}
          onClose={() => this.closeModal()}>
          <h1 className={styles.modalHeader}>Region Settings</h1>
          <h2>Only Follow</h2>
          <form>
            {this.state.regions.map(region => (
              <label key={region} for={region} className={styles.control}>
                <input type="checkbox" id={region} /> {' ' + region}
              </label>
            ))}
          </form>
          <button className={styles.update} onClick={() => this.closeModal()}>
            Update
          </button>
          <button className={styles.cancel} onClick={() => this.closeModal()}>
            Cancel
          </button>
        </Modal>
        <Modal
          isOpen={this.state.activeModal === 'language'}
          onClose={() => this.closeModal()}>
          <h1 className={styles.modalHeader}>Language Settings</h1>
          <h2>Only Follow</h2>
          <form>
            {this.state.languages.map(language => (
              <label key={language} for={language} className={styles.control}>
                <input type="checkbox" id={language} /> {' ' + language}
              </label>
            ))}
          </form>
          <button className={styles.update} onClick={() => this.closeModal()}>
            Update
          </button>
          <button className={styles.cancel} onClick={() => this.closeModal()}>
            Cancel
          </button>
        </Modal>
        <Modal
          isOpen={this.state.activeModal === 'nsfw'}
          onClose={() => this.closeModal()}>
          <h1 className={styles.modalHeader}>NSFW Settings</h1>
          <h2>Unfollow</h2>
          <form>
            {this.state.nsfws.map(nsfw => (
              <label key={nsfw} for={nsfw} className={styles.control}>
                <input type="checkbox" id={nsfw} /> {' ' + nsfw}
              </label>
            ))}
          </form>
          <button className={styles.update} onClick={() => this.closeModal()}>
            Update
          </button>
          <button className={styles.cancel} onClick={() => this.closeModal()}>
            Cancel
          </button>
        </Modal>
        <Modal
          isOpen={this.state.activeModal === 'misc'}
          onClose={() => this.closeModal()}>
          <h1 className={styles.modalHeader}>Miscellaneous Settings</h1>
          <h2>Unfollow</h2>
          <form>
            {this.state.misc.map(mis => (
              <label key={mis} for={mis} className={styles.control}>
                <input type="checkbox" id={mis} /> {' ' + mis}
              </label>
            ))}
          </form>
          <button className={styles.update} onClick={() => this.closeModal()}>
            Update
          </button>
          <button className={styles.cancel} onClick={() => this.closeModal()}>
            Cancel
          </button>
        </Modal>
        <div className={styles.flexContainer}>
          <a className={styles.region} onClick={() => this.openModal('region')}>
            <div>Region Settings</div>
          </a>
          <a
            className={styles.language}
            onClick={() => this.openModal('language')}>
            Language Settings
          </a>
          <a className={styles.nsfw} onClick={() => this.openModal('nsfw')}>
            NSFW Settings
          </a>
          <a className={styles.misc} onClick={() => this.openModal('misc')}>
            Misc. Unfollows
          </a>
        </div>
      </div>
    );
  }
}
