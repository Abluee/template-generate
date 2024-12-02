<template>
  <div class="card">
    <h2>${title}</h2>
    <p>${description}</p>
    <div class="items">
      <#list items as item>
        <item-component :data="${item}" />
      </#list>
    </div>
  </div>
</template>
