<template>
    <div class="row">
      <Picker :data="emojiIndex" set="twitter" @select="showEmoji" />
    </div>
  
    <div class="row">
      <div>
        {{ emojisOutput }}
      </div>
    </div>
  </template>
  
  <script>

  import data from "emoji-mart-vue-fast/data/all.json";

  import "emoji-mart-vue-fast/css/emoji-mart.css";

  import { Picker, EmojiIndex } from "emoji-mart-vue-fast/src";

  let emojiIndex = new EmojiIndex(data);
  
  export default {
    name: "ChatEmojiComponent",
    components: {
      Picker
    },
  
    data() {
      return {
        emojiIndex: emojiIndex,
        emojisOutput: ""
      };
    },
  
    methods: {
      showEmoji(emoji) {
        this.emojisOutput = this.emojisOutput + emoji.native;
      }
    }
  };
  </script>
  
  <style>
  .row { display: flex; }
  .row > * { margin: auto; }
  </style>